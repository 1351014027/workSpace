package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saving.metadata.pojo.LabelMapper;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 陈志强
 * @since 2020-06-08
 */
public interface LabelMapperService extends IService<LabelMapper> {

    void saves(List<LabelMapper> lists);
}
