package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saving.metadata.param.LabelParam;
import com.saving.metadata.pojo.Label;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 陈志强
 * @since 2020-06-08
 */
public interface LabelService extends IService<Label> {

    void deleteByList(List<String> ids);

    Page<Label> listPage(int pageNum, int pageSize, Label label);

    void updateParam(LabelParam labelParam);

    Label saveParam(LabelParam labelParam);

    void checkViewExist(String labelName);

    void checkViewSourceExist(String source);

    Page<Map<String, Object>> showLabelData(int pageNum, int pageSize, String id, String str);
}
