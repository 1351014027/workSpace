package com.saving.metadata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.dao.OptionMapper;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.pojo.Hierarchy;
import com.saving.metadata.pojo.Option;
import com.saving.metadata.service.HierarchyService;
import com.saving.metadata.service.OptionService;
import com.saving.metadata.utils.DateUtil;
import com.saving.metadata.utils.LambdaUtils;
import com.saving.metadata.utils.LevelUtil;
import com.saving.metadata.utils.UUID64Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class OptionServiceImpl extends ServiceImpl<OptionMapper, Option> implements OptionService {

    private final HierarchyService hierarchyService;

    private final OptionMapper optionMapper;

    @Autowired
    public OptionServiceImpl(OptionMapper optionMapper, HierarchyService hierarchyService) {
        this.optionMapper = optionMapper;
        this.hierarchyService = hierarchyService;
    }


    @Override
    public void importExcel(List<Map<Integer, String>> listMap, String hierarchyId, String remark, Boolean isCloud) {
        ArrayList<Option> hierarchies = Lists.newArrayList();
        ArrayList<String> keys = Lists.newArrayList(), names = Lists.newArrayList();
        Map<String, String> keyMaps = Maps.newHashMap(), nameMaps = Maps.newHashMap();
        StringBuffer errMsg = new StringBuffer();
        AtomicInteger num = new AtomicInteger(0);
        Hierarchy byId = hierarchyService.getById(hierarchyId);
        if (byId == null) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        int count = count(new QueryWrapper<Option>().lambda()
                .eq(Option::getHierarchyKey, hierarchyId).eq(Option::getIsDelete, 0));
        listMap.forEach(LambdaUtils.consumerWithIndex((obj, index) -> {
            if (StringUtils.isEmpty(obj.get(0))) {
                errMsg.append("第" + (index + 1) + "行记录中的选项代码不能为空;");
            } else if (StringUtils.isEmpty(obj.get(1))) {
                errMsg.append("第").append((index + 1)).append("行记录中的选项名称不能为空;");
            } else if (StringUtils.isNotEmpty(nameMaps.get(obj.get(1))) || StringUtils.isNotEmpty(keyMaps.get(obj.get(0)))) {
                errMsg.append("第").append((index + 1)).append("行记录中的选项名称和选项代码必须唯一,导入文件中存在重复;");
            } else {
                num.getAndIncrement();
            }
            keyMaps.put(obj.get(0), obj.get(1));
            nameMaps.put(obj.get(1), obj.get(0));
            Option build = Option.builder().key(obj.get(0).trim())
                    .id(UUID64Utils.get64UUIDString())
                    .name(obj.get(1).trim())
                    .remark(remark)
                    .sort(String.valueOf(count + num.get()))
                    .hierarchyId(StringUtils.isNotEmpty(hierarchyId) ? hierarchyId : null)
                    .hierarchyKey(byId.getTypeName() + "(" + byId.getTypeKey() + ")")
                    .depth(0)
                    .cdmpVersion(DateUtil.getYyyyMmDd())
                    .level(LevelUtil.ROOT)
                    .build();
            if (isCloud) {
                build.setIsStandard(1);
            }
            keys.add(obj.get(0));
            names.add(obj.get(1));
            hierarchies.add(build);
        }));
        if (errMsg.length() > 0) {
            throw new ParamException(errMsg.toString());
        }
        List<Option> list = list(new QueryWrapper<Option>().lambda().and(obj -> obj.in(Option::getKey, keys).in(Option::getName, names))
                .eq(Option::getSchoolCode, RequestHolder.getCurrenSysUser().getSchoolCode()).eq(Option::getHierarchyId, hierarchyId));
        list.forEach(obj -> errMsg.append("记录中的").append(obj.getKey()).append("(").append(obj.getName()).append("）存在重复记录"));
        if (errMsg.length() > 0) {
            throw new ParamException(errMsg.toString());
        }
        saveBatch(hierarchies, 100);
    }

    @Override
    public Option checkOptionValue(String sjgghdqmc, String scgbm) {
        return optionMapper.checkOptionValue(sjgghdqmc, scgbm);
    }
}
