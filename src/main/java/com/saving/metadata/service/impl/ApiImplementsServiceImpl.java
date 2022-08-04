package com.saving.metadata.service.impl;

import com.google.common.collect.Lists;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.dto.ApiParamDto;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.pojo.ApiTable;
import com.saving.metadata.service.ApiImplementsService;
import com.saving.metadata.service.ApiTableService;
import com.saving.metadata.utils.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author: 陈志强
 * @date: 2020/7/3 10:47
 * @Description:
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ApiImplementsServiceImpl implements ApiImplementsService {

    /**
     * 新增类型
     */
    private final String OPERATION_TYPE_INSERT = "insert";
    /**
     * 删除类型
     */
    private final String OPERATION_TYPE_DELETE = "delete";
    /**
     * 更新类型
     */
    private final String OPERATION_TYPE_UPDATE = "update";
    /**
     * 查询类型
     */
    private final String OPERATION_TYPE_FIND = "find";

    @Resource
    private ApiTableService apiTableService;


    @Override
    public String getToken(String schoolCode, String apiKey, String softwareNumber) {
        ApiTable apiTable = apiTableService.findByRjbh(softwareNumber);
        if (apiTable == null || !apiTable.getKey().equals(apiKey)) {
            throw new ParamException("获取加密密钥失败!请检查apiKey和schoolCode、softwareNumber参数是否正确!");
        }
        try {
            return SsoUtil.getToken(schoolCode, apiTable.getKey());
        } catch (IOException e) {
            log.error("获取加密密钥失败!错误为:" + e, e);
            throw new ParamException("获取加密密钥失败!请检查softwareNumber和schoolCode参数是否正确!");
        }
    }

    @Override
    public ServerResponse operationTable(ApiParamDto apiParamDto) {
        checkOperationType(apiParamDto.getOperationType());
        checkToken(apiParamDto.getApiKey(), apiParamDto.getSchoolCode(), apiParamDto.getSoftwareNumber());
        if (OPERATION_TYPE_INSERT.equals(apiParamDto.getOperationType())) {
            return ServerResponse.createBySuccess("成功新增" + apiTableService.apiInsertData(apiParamDto) + "条数据!");
        } else if (OPERATION_TYPE_DELETE.equals(apiParamDto.getOperationType())) {
            return ServerResponse.createBySuccess("成功删除" + apiTableService.apiDeleteData(apiParamDto) + "条数据!");
        } else if (OPERATION_TYPE_UPDATE.equals(apiParamDto.getOperationType())) {
            return ServerResponse.createBySuccess("成功更新" + apiTableService.apiUpdateData(apiParamDto) + "条数据!");
        } else if (OPERATION_TYPE_FIND.equals(apiParamDto.getOperationType())) {
            return apiTableService.apiFindData(apiParamDto);
        }
        return ServerResponse.createBySuccess("暂无修改操作!");
    }

    private void checkOperationType(String operationType) {
        List<String> list = Lists.newArrayList(OPERATION_TYPE_INSERT, OPERATION_TYPE_DELETE, OPERATION_TYPE_UPDATE, OPERATION_TYPE_FIND);
        if (StringUtils.isBlank(operationType) || !list.contains(operationType)) {
            log.error("尝试操作API接口错误,操作类型operationType不正确!");
            throw new ParamException("操作类型operationType不正确");
        }
    }

    private void checkToken(String apiKey, String schoolCode, String softwareNumber) {
        ApiTable apiTable = apiTableService.findByRjbh(softwareNumber);
        if (apiTable == null || apiTable.getKey().equals(apiKey)) {
            throw new ParamException("请检查apiKey和schoolCode、softwareNumber参数是否正确!");
        }
        try {
            if (StringUtils.isBlank(apiKey) || !SsoUtil.getToken(schoolCode, apiTable.getKey()).equals(apiKey)) {
                throw new ParamException("token已失效或者无效的token!");
            }
        } catch (IOException e) {
            log.error("获取加密密钥失败!错误为:" + e, e);
            throw new ParamException("获取加密密钥失败!请检查softwareNumber和schoolCode参数是否正确!");
        }

    }
}
