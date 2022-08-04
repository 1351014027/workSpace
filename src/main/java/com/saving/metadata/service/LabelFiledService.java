package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saving.metadata.param.LabelFiledParam;
import com.saving.metadata.pojo.LabelFiled;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 陈志强
 * @since 2020-06-08
 */
public interface LabelFiledService extends IService<LabelFiled> {

    void deleteByList(List<String> ids);

    Page<LabelFiled> listPage(int pageNum, int pageSize, LabelFiled labelFiled);

    void updateParam(LabelFiledParam labelParam);

    void saveParam(LabelFiledParam labelParam);

    void savesParam(List<LabelFiledParam> labelParam, Boolean isCheckSource);
}
