package com.saving.metadata.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.pojo.BackupMessage;
import com.saving.metadata.service.BackupMessageService;
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
 * @since 2020-02-11
 */
@Slf4j
@RestController
@RequestMapping("/metadata/backupMessage")
@Api(tags = "数据库备份记录相关接口")
public class BackupMessageController {

    private final BackupMessageService backupMessageService;


    @Autowired
    public BackupMessageController(BackupMessageService backupMessageService) {
        this.backupMessageService = backupMessageService;
    }


    @PostMapping("listPage.json")
    @ApiOperation(value = "数据备份信息分页查询接口", notes = "物理分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", required = true, paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "操作日期开始时间"),
            @ApiImplicitParam(name = "endDate", value = "操作日期结束时间"),
            @ApiImplicitParam(name = "userName", value = "操作人卡号或者姓名")
    })
    public ServerResponse<Page<BackupMessage>> listPage(
            @RequestParam(defaultValue = ResponseCode.PAGENUM) int pageNum
            , @RequestParam(defaultValue = ResponseCode.PAGESIZE) int pageSize
            , String userName
            , String startDate
            , String endDate) {
        Page<BackupMessage> page = backupMessageService.page(new Page<>(pageNum, pageSize), new QueryWrapper<BackupMessage>().lambda()
                .and(StringUtils.isNotEmpty(userName), obj -> obj
                        .like(StringUtils.isNotEmpty(userName), BackupMessage::getOperationUserName, userName).or()
                        .like(StringUtils.isNotEmpty(userName), BackupMessage::getOperationUser, userName))
                .between(StringUtils.isNoneBlank(startDate) && StringUtils.isNoneBlank(endDate), BackupMessage::getBackupTime, startDate, endDate).orderByDesc(BackupMessage::getBackupTime));
        return ServerResponse.createBySuccess(page);
    }

    @PostMapping(value = "backupData.json")
    @ApiOperation(value = "备份数据接口", notes = "备份数据")
    public ServerResponse backupData() {
        backupMessageService.backupData();
        return ServerResponse.createBySuccess();
    }

}

