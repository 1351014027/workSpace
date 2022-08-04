package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saving.metadata.pojo.Tysjgl;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 陈志强
 * @since 2020-05-03
 */
public interface TysjglService extends IService<Tysjgl> {

    void saveCheck(Tysjgl param);

    void createDataBase(String id);

    void delDataBase(List<String> ids);

    void updateCheck(Tysjgl param);
}
