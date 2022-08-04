package com.saving.metadata.controller;

import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.pojo.AntV;
import com.saving.metadata.service.AntVService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/AntV")
@Api(tags = "AntV表关系数据获取相关接口")
public class AntDataController {

    @Resource
    private AntVService antVService;

    @PostMapping(value = "getData.json")
    @ApiOperation(value = "用于提供表关系数据的接口", notes = "物理分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "tableId", value = "表ID", required = true, paramType = "query"),
    })
    public ServerResponse<List<AntV>> getData(String tableId) {
        List<AntV> gdata = antVService.getData(tableId);
        return ServerResponse.createBySuccess(gdata);
    }

    @PostMapping(value = "getChartsData.json")
    @ApiOperation(value = "用于提供表关系数据的接口", notes = "物理分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "tableId", value = "表ID", required = true, paramType = "query"),
    })
    public ServerResponse<List<List<Map<String, String>>>> getChartsData(String tableId) {
        List<List<Map<String, String>>> gdata = antVService.getChartsData(tableId);
        return ServerResponse.createBySuccess(gdata);
    }
}
