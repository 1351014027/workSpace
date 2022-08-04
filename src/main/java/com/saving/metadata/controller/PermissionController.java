package com.saving.metadata.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.pojo.Permission;
import com.saving.metadata.pojo.User;
import com.saving.metadata.service.PermissionService;
import com.saving.metadata.utils.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 陈志强
 * @since 2020-03-15
 */
@RestController
@RequestMapping("/metadata/permission")
@Api(tags = "权限跳转相关接口")
@Slf4j
public class PermissionController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping(value = "update.json")
    @ApiOperation(value = "更新权限接口", notes = "更新类型")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "param", value = "选项对象，用于更新", required = true, paramType = "query")
    })
    public ServerResponse update(@RequestBody Map<String, String> map) {
        String kh = map.get("kh");
        String xm = map.get("xm");
        map.remove("kh");
        map.remove("xm");
        permissionService.remove(new QueryWrapper<Permission>().lambda().eq(Permission::getUserName, kh));
        permissionService.save(kh, xm, map);
        return ServerResponse.createBySuccess();
    }

    @PostMapping(value = "save.json")
    @ApiOperation(value = "新增权限接口", notes = "新增类型")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "map", value = "对象，用于新增", required = true, paramType = "query")
    })
    public ServerResponse save(@RequestBody Map<String, String> map) {
        String kh = map.get("kh");
        String xm = map.get("xm");
        map.remove("kh");
        map.remove("xm");
        permissionService.save(kh, xm, map);
        return ServerResponse.createBySuccess();
    }

    @PostMapping(value = "deletes.json")
    @ApiOperation(value = "批量删除类型接口", notes = "字段删除")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids", value = "选项对象主键组", required = true, paramType = "query")
    })
    public ServerResponse del(@RequestBody List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        if (!ids.isEmpty()) {
            permissionService.remove(new QueryWrapper<Permission>().lambda().in(Permission::getUserName, ids));
            HttpServletRequest request = RequestHolder.getCurrenRequest();
            User user = RequestHolder.getCurrenSysUser();
            log.error("【数据中台系统】卡号为{}的用户删除了ID为{}的权限数据，删除记录数量为{},删除IP为{}", user.getUsername(), ids, ids.size(), IpUtils.getIpAddr(request));

        }
        return ServerResponse.createBySuccess();
    }


    @PostMapping("listPage.json")
    @ApiOperation(value = "权限分页查询接口", notes = "物理分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", required = true, paramType = "query"),
            @ApiImplicitParam(name = "str", value = "根据卡号或者姓名过滤")
    })
    public ServerResponse<Page<Permission>> listPage(
            @RequestParam(defaultValue = ResponseCode.PAGENUM) int pageNum
            , @RequestParam(defaultValue = ResponseCode.PAGESIZE) int pageSize
            , String str) {
        return ServerResponse.createBySuccess(permissionService.listPage(new Page<>(pageNum, pageSize), str));
    }

}

