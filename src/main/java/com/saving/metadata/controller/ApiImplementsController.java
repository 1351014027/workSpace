package com.saving.metadata.controller;

import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.dto.ApiParamDto;
import com.saving.metadata.service.ApiImplementsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: 陈志强
 * @date: 2020/7/3 10:44
 * @Description:
 */
@RestController
@RequestMapping("/metadata/ApiImplements")
@Slf4j
@Api(tags = "软件资产信息控制器")
public class ApiImplementsController {


    @Resource
    private ApiImplementsService apiImplementsService;

    /**
     * 编写公用API接口网关
     *
     * @return 返回影响行数或者结果集
     */
    @PostMapping("apiGateway.json")
    public ServerResponse apiGateway(@RequestBody ApiParamDto apiParamDto) {

        return apiImplementsService.operationTable(apiParamDto);
    }

    /**
     * 效验APIkey 返回token 用于网关效验
     *
     * @return token
     */
    @PostMapping("getApiKey.json")
    public ServerResponse<String> getApiKey(String schoolCode, String apiKey, String softwareNumber) {
        return ServerResponse.createBySuccess(apiImplementsService.getToken(schoolCode, apiKey, softwareNumber));
    }

}
