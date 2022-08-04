package com.saving.metadata.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.param.LabelParam;
import com.saving.metadata.pojo.Label;
import com.saving.metadata.pojo.User;
import com.saving.metadata.service.LabelService;
import com.saving.metadata.utils.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 陈志强
 * @since 2020-06-08
 */
@RestController
@RequestMapping("/metadata/label")
@Api(tags = "标签控制器接口")
@Slf4j
public class LabelController<T> {

    @Resource
    private LabelService labelService;

    @PostMapping("save.json")
    @ApiOperation(value = "新增接口", notes = "新增接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "labelParam", value = "标签效验参数对象", required = true, paramType = "query"),
    })
    public ServerResponse<Label> save(LabelParam labelParam, @RequestParam(defaultValue = "false") Boolean isCheck) {
        if (isCheck) {
            if (StringUtils.isNotBlank(labelParam.getLabelName()) && StringUtils.isNotBlank(labelParam.getDatabaseName())) {
                labelService.checkViewExist(labelParam.getLabelName());
                labelService.checkViewSourceExist(labelParam.getDatabaseName());
            } else {
                throw new ParamException(ResponseCode.ERRORPARAM);
            }
        }
        return ServerResponse.createBySuccess(labelService.saveParam(labelParam));
    }


    @PostMapping("update.json")
    @ApiOperation(value = "更新接口", notes = "更新接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "labelParam", value = "标签效验参数对象", required = true, paramType = "query"),
    })
    public ServerResponse<T> update(LabelParam labelParam, @RequestParam(defaultValue = "false") Boolean isCheck) {
        if (isCheck) {
            if (StringUtils.isNotBlank(labelParam.getLabelName()) && StringUtils.isNotBlank(labelParam.getDatabaseName())) {
                labelService.checkViewExist(labelParam.getLabelName());
                labelService.checkViewSourceExist(labelParam.getDatabaseName());
            } else {
                throw new ParamException(ResponseCode.ERRORPARAM);
            }
        }
        labelService.updateParam(labelParam);
        return ServerResponse.createBySuccess();
    }


    @PostMapping("del.json")
    @ApiOperation(value = "批量删除接口", notes = "批量根据ID进行删除")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids", value = "主键ID数组", required = true, paramType = "query"),
    })
    public ServerResponse<T> delete(@RequestBody List<String> ids) {
        labelService.deleteByList(ids);
        HttpServletRequest request = RequestHolder.getCurrenRequest();
        User user = RequestHolder.getCurrenSysUser();
        log.error("【数据中台系统】卡号为{}的用户删除了ID为{}的标签数据，删除记录数量为{},删除IP为{}", user.getUsername(), ids, ids.size(), IpUtils.getIpAddr(request));
        return ServerResponse.createBySuccess();
    }


    @PostMapping("list.json")
    @ApiOperation(value = "分页查询接口", notes = "物理分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", required = true, paramType = "query"),
            @ApiImplicitParam(name = "label", value = "标签对象，用于过滤")
    })
    public ServerResponse<Page<Label>> listPage(@RequestParam(defaultValue = "1") int pageNum,
                                                @RequestParam(defaultValue = "1") int pageSize,
                                                Label label) {
        Page<Label> labelPage = labelService.listPage(pageNum, pageSize, label);
        return ServerResponse.createBySuccess(labelPage);
    }

    @PostMapping("showLabelDataTable.json")
    @ApiOperation(value = "分页查询接口", notes = "物理分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", required = true, paramType = "query"),
            @ApiImplicitParam(name = "label", value = "标签对象，用于过滤")
    })
    public ServerResponse<Page<Map<String, Object>>> showLabelDataTable(@RequestParam(defaultValue = "1") int pageNum,
                                                                        @RequestParam(defaultValue = "1") int pageSize,
                                                                        String id, String str) {

        return ServerResponse.createBySuccess(labelService.showLabelData(pageNum, pageSize, id, str));
    }


}

