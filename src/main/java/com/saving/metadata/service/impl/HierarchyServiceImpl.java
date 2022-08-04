package com.saving.metadata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.dao.HierarchyMapper;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.param.HierarchyParam;
import com.saving.metadata.pojo.Hierarchy;
import com.saving.metadata.pojo.Label;
import com.saving.metadata.service.HierarchyService;
import com.saving.metadata.service.LabelService;
import com.saving.metadata.utils.*;
import com.saving.metadata.vo.BasicData;
import com.saving.metadata.vo.CheckArr;
import com.saving.metadata.vo.LayuiTreeVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 陈志强
 * @since 2019-12-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HierarchyServiceImpl extends ServiceImpl<HierarchyMapper, Hierarchy> implements HierarchyService {

    private static final String TRUE = "true";
    private static final List<Hierarchy> childMenuList = Lists.newArrayList();
    @Resource
    private HierarchyMapper hierarchyMapper;
    @Resource
    private LabelService labelService;
    private final Map<Integer, Hierarchy> maps = Maps.newLinkedHashMap();


    private static List<Hierarchy> treeMenuList(List<Hierarchy> menuList, String pid) {
        for (Hierarchy mu : menuList) {
            if (mu.getParent().equals(pid)) {
                treeMenuList(menuList, mu.getId());
                childMenuList.add(mu);
            }
        }
        return childMenuList;
    }

    @Override
    public void save(HierarchyParam param, Boolean isCloud) {
        Hierarchy hierarchy = Hierarchy.builder().build();
        if (checkExist(param.getTypeKey(), param.getTypeName(), null, param.getParent())) {
            throw new ParamException("同一层级下存在相同的名称或名称");
        }
        getNewHierarchy(param, hierarchy);
        hierarchy.setCdmpVersion(DateUtil.getYyyyMmDd());
        hierarchy.setId(UUID64Utils.get64UUIDString());
        if (isCloud) {
            hierarchy.setIsStandard(1);
        }
        hierarchyMapper.insert(hierarchy);
    }

    @Override
    public void update(HierarchyParam param) {
        BeanValidator.check(param);
        if (StringUtils.isEmpty(param.getId())) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        if (checkExist(param.getTypeKey(), param.getTypeName(), param.getId(), param.getParent())) {
            throw new ParamException("同一层级下存在相同的名称");
        }
        Hierarchy before = hierarchyMapper.selectById(param.getId());
        if (before.getSortId().equals(param.getParent())) {
            throw new ParamException("不能父级类目设置为自身");
        }
        Hierarchy hierarchy = Hierarchy.builder().build();
        getNewHierarchy(param, hierarchy);
        if (StringUtils.isEmpty(param.getRemark())) {
            hierarchy.setRemark("");
        }
        hierarchy.setCdmpVersion(DateUtil.getYyyyMmDd());
        hierarchyMapper.updateById(hierarchy);
    }

    @Override
    public List<Hierarchy> treeList(Hierarchy hierarchy) {
        hierarchy.setSchoolCode(RequestHolder.getCurrenSysUser().getSchoolCode());
        List<Hierarchy> list = hierarchyMapper.selectList(new QueryWrapper<Hierarchy>()
                .likeRight(StringUtils.isNotEmpty(hierarchy.getLevel()), "Level_", hierarchy.getLevel())
                .eq(hierarchy.getParent() != null, "Parent_", hierarchy.getParent())
                .orderByAsc(" cast(Sort_ as int) "));
        list.forEach(obj -> maps.put(obj.getSortId(), obj));
        return treeHierarchyList(list, 0);
    }

    @Override
    public void importExcel(List<Map<Integer, String>> listMap, String parent, String remark, Boolean isCloud) {
        ArrayList<Hierarchy> hierarchies = Lists.newArrayList();
        ArrayList<String> keys = Lists.newArrayList(), names = Lists.newArrayList();
        Map<String, String> keyMaps = Maps.newHashMap(), nameMaps = Maps.newHashMap();
        Hierarchy parents = hierarchyMapper.selectOne(new QueryWrapper<Hierarchy>().lambda().eq(Hierarchy::getSortId, parent).eq(Hierarchy::getSchoolCode, RequestHolder.getCurrenSysUser().getSchoolCode()));
        Integer selectCount = hierarchyMapper.selectCount(new QueryWrapper<Hierarchy>().lambda().eq(StringUtils.isNotEmpty(parent), Hierarchy::getParent, parent).eq(Hierarchy::getIsDelete, 0));
        StringBuffer errMsg = new StringBuffer();
        AtomicInteger num = new AtomicInteger(0);
        listMap.forEach(LambdaUtils.consumerWithIndex((obj, index) -> {
            if (StringUtils.isEmpty(obj.get(0))) {
                errMsg.append("第" + index + 1 + "行记录中的类型代码不能为空;");
            } else if (StringUtils.isEmpty(obj.get(1))) {
                errMsg.append("第" + index + 1 + "行记录中的类型名称不能为空;");
            } else if (StringUtils.isNotEmpty(nameMaps.get(obj.get(1))) || StringUtils.isNotEmpty(keyMaps.get(obj.get(0)))) {
                errMsg.append("第").append((index + 1)).append("行记录中的类型名称和类型代码必须唯一,导入文件中存在重复;");
            } else {
                num.getAndIncrement();
            }
            keyMaps.put(obj.get(0), obj.get(1));
            nameMaps.put(obj.get(1), obj.get(0));
            Hierarchy build = Hierarchy.builder().typeKey(obj.get(0).trim())
                    .id(UUID64Utils.get64UUIDString())
                    .typeName(obj.get(1).trim())
                    .remark(remark)
                    .sort(String.valueOf(selectCount + num.get()))
                    .parent(StringUtils.isNotEmpty(parent) ? Integer.parseInt(parent) : 0)
                    .depth(parents == null ? 0 : parents.getDepth() + 1)
                    .level(parents == null ? LevelUtil.ROOT : LevelUtil.calculateLevel(parents.getLevel(), parents.getSortId()))
                    .cdmpVersion(DateUtil.getYyyyMmDd())
                    .build();
            keys.add(obj.get(0));
            names.add(obj.get(1));
            if (isCloud) {
                build.setIsStandard(1);
            }
            hierarchies.add(build);
        }));
        if (errMsg.length() > 0) {
            throw new ParamException(errMsg.toString());
        }
        List<Hierarchy> list = list(new QueryWrapper<Hierarchy>().lambda().and(obj -> obj.in(Hierarchy::getTypeKey, keys).in(Hierarchy::getTypeName, names))
                .eq(Hierarchy::getSchoolCode, RequestHolder.getCurrenSysUser().getSchoolCode()).eq(parents != null, Hierarchy::getParent, parent));
        list.stream().forEach(obj -> errMsg.append("记录中的").append(obj.getTypeKey()).append("(").append(obj.getTypeName()).append("）"));
        if (errMsg.length() > 0) {
            throw new ParamException(errMsg.toString());
        }
        saveBatch(hierarchies, 220);
    }

    @Override
    public List<LayuiTreeVo> lastList(String nodeId, String parentId, String level, String isReload, String schoolCode, Boolean isTag) {
        if (StringUtils.isBlank(level) || TRUE.equals(isReload)) {
            List<Hierarchy> hierarchyList = list(new QueryWrapper<Hierarchy>().eq("Parent_", "0").eq("SchoolCode_", schoolCode).orderByAsc("cast(Sort_ as int)"));
            return assemble(hierarchyList, schoolCode, isTag);
        }
        if (StringUtils.isNoneBlank(nodeId)) {
            List<Hierarchy> hierarchies = list(new QueryWrapper<Hierarchy>().eq("Parent_", nodeId).eq("SchoolCode_", schoolCode).orderByAsc("cast(Sort_ as int)"));
            if (CollectionUtils.isNotEmpty(hierarchies)) {
                return assemble(hierarchies, schoolCode, isTag);
            } else if (isTag) {
                Hierarchy parentHierarchy = getOne(new QueryWrapper<Hierarchy>().lambda().eq(Hierarchy::getSortId, nodeId));
                List<Label> labelList = labelService.list(new QueryWrapper<Label>().eq("Hierarchy_ID_", parentHierarchy.getId()).eq("SchoolCode_", schoolCode).orderByAsc("cast(Sort_ as int)"));
                return assembleLabel(labelList, nodeId);
            }

        }
        return Lists.newArrayList();
    }


    private void getNewHierarchy(HierarchyParam param, Hierarchy hierarchy) {
        BeanUtils.copyProperties(param, hierarchy);
        Hierarchy parent = hierarchyMapper.selectOne(new QueryWrapper<Hierarchy>().lambda().eq(Hierarchy::getSortId, param.getParent()).eq(Hierarchy::getSchoolCode, RequestHolder.getCurrenSysUser().getSchoolCode()));
        hierarchy.setDepth(parent == null ? 0 : parent.getDepth() + 1);
        hierarchy.setLevel(parent == null ? LevelUtil.ROOT : LevelUtil.calculateLevel(parent.getLevel(), parent.getSortId()));
        hierarchy.setParent(param.getParent() == null ? 0 : param.getParent());
    }

    private Boolean checkExist(String typeKey, String typeName, String id, Integer parent) {
        return hierarchyMapper.selectCount(new QueryWrapper<Hierarchy>().lambda().and(wrapper -> wrapper.eq(Hierarchy::getTypeKey, typeKey).or().eq(Hierarchy::getTypeName, typeName)).eq(Hierarchy::getParent, parent == null ? 0 : parent).ne(StringUtils.isNotEmpty(id), Hierarchy::getId, id)) > 0;
    }

    private List<Hierarchy> treeHierarchyList(List<Hierarchy> menuList, Integer pid) {
        for (Hierarchy mu : menuList) {
            if (mu.getParent().equals(pid)) {
                if (mu.getParent() != 0) {
                    mu.setSort(maps.get(mu.getParent()).getSort() + '-' + mu.getSort());
                }
                treeHierarchyList(menuList, mu.getSortId());
            }
        }
        return menuList;
    }

    private List<LayuiTreeVo> assemble(List<Hierarchy> list, String schoolCode, Boolean isTag) {
        List<LayuiTreeVo> results = Lists.newArrayList();
        for (Hierarchy hierarchy : list) {
            String title;
            title = hierarchy.getTypeName();
            LayuiTreeVo treeVo = builderVo(hierarchy.getSortId().toString(), title, null, hierarchy.getParent().toString(), BasicData.builder().xzz(String.valueOf(hierarchy.getId())).build());
            if (list(new QueryWrapper<Hierarchy>().lambda().eq(Hierarchy::getParent, hierarchy.getSortId()).eq(Hierarchy::getSchoolCode, schoolCode)).isEmpty()) {
                treeVo.setLast(true);
                treeVo.setChildren(Lists.newArrayList());
            }
            if (isTag && !labelService.list(new QueryWrapper<Label>().lambda().eq(Label::getHierarchyId, hierarchy.getId())).isEmpty()) {
                treeVo.setLast(false);
                treeVo.setChildren(Lists.newArrayList());
            }
            results.add(treeVo);
        }
        return results;
    }

    private List<LayuiTreeVo> assembleLabel(List<Label> labelList, String parentId) {
        List<LayuiTreeVo> results = Lists.newArrayList();
        for (Label label : labelList) {
            LayuiTreeVo treeVo = builderVo(label.getId(), label.getLabelZnName(), null, parentId, BasicData.builder()
                    .bcwjm(label.getLabelName()).xzz(label.getLabelNameDescribe()).isRw(true).build());
            treeVo.setLast(true);
            treeVo.setChildren(Lists.newArrayList());
            results.add(treeVo);
        }
        return results;
    }

    private LayuiTreeVo builderVo(String id, String title, List<LayuiTreeVo> list, String parentId, BasicData basicData) {
        List<CheckArr> objects = Lists.newArrayList();
        objects.add(new CheckArr());
        return LayuiTreeVo.builder()
                .id(id).last(false)
                .title(title).children(list).checkArr(objects).basicData(basicData)
                .parentId(parentId).build();
    }

}
