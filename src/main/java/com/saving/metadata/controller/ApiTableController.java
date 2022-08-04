package com.saving.metadata.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.listener.UpdateApiTableFiledsListener;
import com.saving.metadata.param.ApiTableParam;
import com.saving.metadata.pojo.ApiTable;
import com.saving.metadata.pojo.User;
import com.saving.metadata.service.ApiTableService;
import com.saving.metadata.utils.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 陈志强
 * @since 2020-04-09
 */
@RestController
@RequestMapping("/metadata/apiTable")
@Slf4j
@Api(tags = "软件资产信息控制器")
public class ApiTableController {

    private final ApiTableService apiTableService;


    @Autowired
    public ApiTableController(ApiTableService apiTableService) {
        this.apiTableService = apiTableService;
    }

    @PostMapping(value = "deletes.json")
    @ApiOperation(value = "批量删除接口", notes = "字段删除")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids", value = "对象主键组", required = true, paramType = "query")
    })
    public ServerResponse<Object> del(@RequestBody List<String> ids) {
        if (!ids.isEmpty()) {
            apiTableService.removeByIds(ids);
            HttpServletRequest request = RequestHolder.getCurrenRequest();
            User user = RequestHolder.getCurrenSysUser();
            log.error("【数据中台系统】卡号为{}的用户删除了ID为{}的版本软件资产数据，删除记录数量为{},删除IP为{}", user.getUsername(), ids, ids.size(), IpUtils.getIpAddr(request));
        }
        return ServerResponse.createBySuccess();
    }

    @PostMapping(value = "update.json")
    @ApiOperation(value = "更新接口", notes = "更新类型")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "MetaDataFileds", value = "对象，用于更新", required = true, paramType = "query")
    })
    public ServerResponse<Object> update(ApiTableParam param) {
        apiTableService.updateCheck(param);
        return ServerResponse.createBySuccess();
    }

    @PostMapping(value = "save.json")
    @ApiOperation(value = "新增接口", notes = "新增类型")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "MetaDataFileds", value = "对象，用于新增", required = true, paramType = "query")
    })
    public ServerResponse<Object> save(ApiTableParam param) {
        apiTableService.saveCheck(param);
        return ServerResponse.createBySuccess();
    }

    @PostMapping("listPage.json")
    @ApiOperation(value = "字段分页查询接口", notes = "物理分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", required = true, paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "过滤上传日期开始时间"),
            @ApiImplicitParam(name = "endDate", value = "过滤上传日期结束时间"),
            @ApiImplicitParam(name = "ApiTable", value = "软件资产对象，用于过滤")
    })
    public ServerResponse<Page<ApiTable>> listPage(
            @RequestParam(defaultValue = ResponseCode.PAGENUM) int pageNum
            , @RequestParam(defaultValue = ResponseCode.PAGESIZE) int pageSize
            , ApiTable apiTable
            , String startDate
            , String endDate) {
        User user = RequestHolder.getCurrenSysUser();
        QueryWrapper<ApiTable> wrapper = new QueryWrapper<ApiTable>().and(StringUtils.isNoneBlank(apiTable.getSysName()), obj -> obj.like(StringUtils.isNoneBlank(apiTable.getSysName()), "Sys_Name_", apiTable.getSysName()).or()
                .like(StringUtils.isNoneBlank(apiTable.getSysName()), "Manufacturers_User_", apiTable.getSysName()))
                .between(StringUtils.isNoneBlank(startDate) && StringUtils.isNoneBlank(endDate), "Create_Time_", startDate, endDate).eq("School_Code_", user.getSchoolCode())
                .orderByAsc("cast(Sort_ as int)")
                .orderByAsc("Sort_Id_")
                .orderByAsc("Create_Time_");
        Page<ApiTable> page = apiTableService.page(new Page<>(pageNum, pageSize), wrapper);
        return ServerResponse.createBySuccess(page);
    }

    @PostMapping("import.json")
    @ResponseBody
    @ApiOperation(value = "批量导入", notes = "批量导入")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "文件流", required = true, paramType = "query"),
    })
    public ServerResponse<? extends Object> importExcel(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), ApiTable.class, new UpdateApiTableFiledsListener(apiTableService)).sheet().doRead();
        return ServerResponse.createBySuccess();
    }


    @PostMapping(value = "syncRjzc.json")
    @ApiOperation(value = "从软件资产标准表中同步", notes = "从软件资产标准表中同步")
    public ServerResponse<Object> syncRjzc() {
        apiTableService.syncRjzc();
        return ServerResponse.createBySuccess();
    }

    @PostMapping(value = "rjzcSync.json")
    @ApiOperation(value = "同步到软件资产标准表中", notes = "同步到软件资产标准表中")
    public ServerResponse<Object> rjzcSync() {
        apiTableService.rjzcSync();
        return ServerResponse.createBySuccess();
    }


}

