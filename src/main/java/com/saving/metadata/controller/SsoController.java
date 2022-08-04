package com.saving.metadata.controller;


import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.exception.PermissionException;
import com.saving.metadata.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 用户前端控制器
 * </p>
 *
 * @author saving
 * @since 2019-12-15
 */
@Controller
@RequestMapping("/sso")
@Slf4j
@Api(tags = "用户相关接口")
public class SsoController {


    private final UserService userService;

    @Autowired
    public SsoController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "ssoXxhx.page")
    public void ssoXxhx(HttpServletResponse response) {
        try {
            response.sendRedirect(userService.ssoXxhx());
        } catch (IOException e) {
            log.error("單點登錄失敗！");
            throw new PermissionException(ResponseCode.ERRORPPERSSION);
        }
    }

    @GetMapping(value = "ssoGljsc.page")
    public void ssoGljsc(HttpServletResponse response) {
        try {
            response.sendRedirect(userService.ssoGljsc());
        } catch (IOException e) {
            log.error("單點登錄失敗！");
            throw new PermissionException(ResponseCode.ERRORPPERSSION);
        }
    }


}

