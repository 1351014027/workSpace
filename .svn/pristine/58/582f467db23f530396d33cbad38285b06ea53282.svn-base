package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saving.metadata.param.SchooleCodeParam;
import com.saving.metadata.pojo.SchooleCode;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 陈志强
 * @since 2020-02-24
 */
public interface SchooleCodeService extends IService<SchooleCode> {

    /**
     * 新增
     *
     * @param param
     */
    void save(SchooleCodeParam param) throws ParseException;

    /**
     * 更新
     *
     * @param param
     */
    void update(SchooleCodeParam param);


    List<SchooleCode> opiton();


    void updateByIdsIsDel(List<String> ids);
}
