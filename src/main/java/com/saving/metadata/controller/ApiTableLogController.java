package com.saving.metadata.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.pojo.ApiTableLog;
import com.saving.metadata.service.ApiTableLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 陈志强
 * @since 2020-04-09
 */
@RestController
@RequestMapping("/metadata/apiTableLog")
@Slf4j
@Api(tags = "API调用记录相关接口")
public class ApiTableLogController {

    private final ApiTableLogService apiTableLogService;


    @Autowired
    public ApiTableLogController(ApiTableLogService apiTableLogService) {
        this.apiTableLogService = apiTableLogService;
    }


    @PostMapping("listPage.json")
    @ApiOperation(value = "操作日志分页查询接口", notes = "物理分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", required = true, paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "操作日期开始时间"),
            @ApiImplicitParam(name = "endDate", value = "操作日期结束时间"),
            @ApiImplicitParam(name = "tableName", value = "操作表名，用于过滤")
    })
    public ServerResponse<Page<ApiTableLog>> listPage(
            @RequestParam(defaultValue = ResponseCode.PAGENUM) int pageNum
            , @RequestParam(defaultValue = ResponseCode.PAGESIZE) int pageSize
            , String tableName
            , String startDate
            , String endDate) {
        Page<ApiTableLog> page = apiTableLogService.page(new Page<>(pageNum, pageSize), new QueryWrapper<ApiTableLog>().lambda()
                .like(StringUtils.isNotEmpty(tableName), ApiTableLog::getTableName, tableName)
                .between(StringUtils.isNoneBlank(startDate) && StringUtils.isNoneBlank(endDate), ApiTableLog::getOperationTime, startDate, endDate).orderByDesc(ApiTableLog::getOperationTime));
        return ServerResponse.createBySuccess(page);
    }

}

