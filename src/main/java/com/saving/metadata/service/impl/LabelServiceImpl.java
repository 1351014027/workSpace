package com.saving.metadata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.dao.LabelFiledMapper;
import com.saving.metadata.dao.LabelMapper;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.param.LabelParam;
import com.saving.metadata.pojo.Label;
import com.saving.metadata.pojo.LabelFiled;
import com.saving.metadata.pojo.User;
import com.saving.metadata.service.LabelService;
import com.saving.metadata.utils.BeanValidator;
import com.saving.metadata.utils.UUID64Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 陈志强
 * @since 2020-06-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements LabelService {

    @Resource
    private LabelMapper labelMapper;

    @Resource
    private LabelFiledMapper labelFiledMapper;


    /**
     * 批量删除接口，根据ID进行批量删除
     *
     * @param ids 主键数组
     */
    @Override
    public void deleteByList(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        removeByIds(ids);
    }

    /**
     * 分页查询
     *
     * @param pageNum  第几页
     * @param pageSize 每页多少条
     * @param label    标签对象
     * @return 返回分页标签对象
     */
    @Override
    public Page<Label> listPage(int pageNum, int pageSize, Label label) {
        return page(new Page<>(pageNum, pageSize), new QueryWrapper<Label>().lambda()
                .eq(StringUtils.isNoneBlank(label.getHierarchyId()), Label::getHierarchyId, label.getHierarchyId())
                .and(StringUtils.isNoneBlank(label.getLabelName()), obj -> obj.like(StringUtils.isNoneBlank(label.getLabelName()), Label::getLabelName, label.getLabelName())
                        .or().like(StringUtils.isNoneBlank(label.getLabelName()), Label::getLabelZnName, label.getLabelName())));
    }

    @Override
    public void updateParam(LabelParam labelParam) {
        BeanValidator.check(labelParam);
        com.saving.metadata.utils.StringUtils.checkStringIsBlankThrowError(labelParam.getId());
        checkExist(labelParam.getId(), labelParam.getLabelName(), labelParam.getLabelZnName());
        Label label = Label.builder().build();
        BeanUtils.copyProperties(labelParam, label);
        User user = RequestHolder.getCurrenSysUser();
        label.setUpdateor(user.getUsername());
        label.setUpdatetime(new Date());
        updateById(label);
    }

    @Override
    public Label saveParam(LabelParam labelParam) {
        BeanValidator.check(labelParam);
        checkExist(null, labelParam.getLabelName(), labelParam.getLabelZnName());
        Label label = Label.builder().build();
        BeanUtils.copyProperties(labelParam, label);
        User user = RequestHolder.getCurrenSysUser();
        label.setId(UUID64Utils.get64UUIDString());
        label.setSchoolcode(user.getSchoolCode());
        label.setCreator(user.getUsername());
        label.setSort(String.valueOf(count()));
        save(label);
        return getById(label.getId());
    }

    @Override
    public void checkViewExist(String labelName) {
        if (labelMapper.checkViewExist(labelName) < 1) {
            throw new ParamException("该标签不存在，请输入正确的标签英文名!");
        }
    }

    @Override
    public void checkViewSourceExist(String source) {
        if (StringUtils.isBlank(labelMapper.checkViewSourceExist(source))) {
            throw new ParamException("该标签数据来源不存在，请输入正确的标签数据来源!");
        }

    }

    @Override
    public Page<Map<String, Object>> showLabelData(int pageNum, int pageSize, String id, String str) {
        Page<Map<String, Object>> page = new Page<>(pageNum, pageSize);
        Label label = getById(id);
        List<LabelFiled> labelFileds = labelFiledMapper.selectList(new QueryWrapper<LabelFiled>()
                .eq("Label_Parent_ID_", label.getId())
                .orderByAsc("cast(Sort_ as int )").orderByAsc("SortId_"));
        List<Map<String, Object>> maps = labelMapper.tableLists(page, (StringUtils.isNoneEmpty(label.getDatabaseName()) ? label.getDatabaseName() : "") + ".." + label.getLabelName(), labelFileds, str);
        page.setRecords(maps);
        return page;
    }

    private void checkExist(String id, String labelName, String labelZnName) {
        if (count(new QueryWrapper<Label>().lambda()
                .ne(StringUtils.isNoneBlank(id), Label::getId, id)
                .and(obj -> obj.eq(Label::getLabelName, labelName).or().eq(Label::getLabelZnName, labelZnName))) > 0) {
            throw new ParamException("已存在相同的标签英文名称或者相同的标签中文名");
        }
    }
}
