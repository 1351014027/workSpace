package com.saving.metadata.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.pojo.User;
import com.saving.metadata.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户前端控制器
 * </p>
 *
 * @author saving
 * @since 2019-12-15
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户相关接口")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(value = "findUser.json")
    @ApiOperation(value = "查询学生接口", notes = "查询学生")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "depId", value = "部门主键", required = true, paramType = "query"),
            @ApiImplicitParam(name = "str", value = "学号或者姓名", required = true, paramType = "query")
    })
    public ServerResponse<List<User>> findUserByDepOrUserName(String depId, String str) {
        if (StringUtils.isNoneEmpty(depId) && StringUtils.isNoneEmpty(depId)) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        List<User> users = userService.list(new QueryWrapper<User>().lambda()
                .eq(StringUtils.isNoneEmpty(depId), User::getDepartmentId, depId)
                .and(StringUtils.isNotEmpty(str), obj -> obj
                        .like(StringUtils.isNotEmpty(str), User::getName, str).or()
                        .like(StringUtils.isNotEmpty(str), User::getUsername, str)));
        ServerResponse<List<User>> success = ServerResponse.createBySuccess(users);
        return success;
    }

    @PostMapping(value = "findDepAll.json")
    @ApiOperation(value = "查询部门接口", notes = "查询学生")
    public ServerResponse<Map<Object, Object>> findDepAll() {
        ServerResponse<Map<Object, Object>> success = ServerResponse.createBySuccess(userService.getDepMaps());
        return success;
    }

}

