package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.dto.ApiParamDto;
import com.saving.metadata.param.ApiTableParam;
import com.saving.metadata.pojo.ApiTable;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 陈志强
 * @since 2020-04-09
 */
public interface ApiTableService extends IService<ApiTable> {

    void updateCheck(ApiTableParam param);

    void saveCheck(ApiTableParam param);

    String checkObj(ApiTable apiTable);

    List<ApiTable> transformOption(List<ApiTable> list);

    void rjzcSync();

    void syncRjzc();

    ApiTable findByRjbh(String rjbh);

    /**
     * API接口查询数据
     *
     * @param apiParamDto api接口网关操作对象
     * @return 查询结果集
     */
    ServerResponse apiFindData(ApiParamDto apiParamDto);

    /**
     * API接口更新数据
     *
     * @param apiParamDto api接口网关操作对象
     * @return 操作影响记录数
     */
    int apiUpdateData(ApiParamDto apiParamDto);

    /**
     * API接口删除数据
     *
     * @param apiParamDto api接口网关操作对象
     * @return 操作影响记录数
     */
    int apiDeleteData(ApiParamDto apiParamDto);

    /**
     * API接口新增数据
     *
     * @param apiParamDto api接口网关操作对象
     * @return 操作影响记录数
     */
    int apiInsertData(ApiParamDto apiParamDto);
}
