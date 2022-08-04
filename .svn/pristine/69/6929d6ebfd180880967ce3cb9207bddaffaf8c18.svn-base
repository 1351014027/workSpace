package com.saving.metadata.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.param.ApiPermissionParam;
import com.saving.metadata.pojo.ApiPermission;
import com.saving.metadata.pojo.User;
import com.saving.metadata.service.ApiPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/metadata/apiPermission")
@Slf4j
@Api(tags = "API接口权限控制器")
public class ApiPermissionController {

    private final ApiPermissionService apiPermissionService;

    @Autowired
    public ApiPermissionController(ApiPermissionService apiPermissionService) {
        this.apiPermissionService = apiPermissionService;
    }

    @PostMapping(value = "save.json")
    @ApiOperation(value = "API接口权限设置接口", notes = "新增类型")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ApiPermissionParam", value = "对象，用于新增", required = true, paramType = "query")
    })
    public ServerResponse save(ApiPermissionParam param) {
        apiPermissionService.saveCheck(param);
        return ServerResponse.createBySuccess();
    }

    @PostMapping("listPage.json")
    @ApiOperation(value = "API接口权限分页查询接口", notes = "物理分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", required = true, paramType = "query"),
            @ApiImplicitParam(name = "apiPermission", value = "对象，用于过滤")
    })
    public ServerResponse<Page<ApiPermission>> listPage(
            @RequestParam(defaultValue = ResponseCode.PAGENUM) int pageNum
            , @RequestParam(defaultValue = ResponseCode.PAGESIZE) int pageSize
            , ApiPermission apiPermission) {
        User user = RequestHolder.getCurrenSysUser();
//        if (StringUtils.isEmpty(apiPermission.getTableId())) {
//            throw new ParamException(ResponseCode.ERRORPARAM);
//        }
        Page<ApiPermission> page = apiPermissionService.getApiPermission(new Page<>(pageNum, pageSize),
                apiPermission.getSysName(), user.getSchoolCode(), apiPermission.getTableId());
        return ServerResponse.createBySuccess(page);
    }


}

