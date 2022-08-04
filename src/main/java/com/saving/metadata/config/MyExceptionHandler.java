package com.saving.metadata.config;

import com.saving.metadata.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author: 陈志强
 * @date: 2019/12/24 15:22
 * @Description:
 */
@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public Map<String, Object> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ServerResponse.createByErrorMessage(e.getMessage()).serverResponseData2Map();
    }
}

