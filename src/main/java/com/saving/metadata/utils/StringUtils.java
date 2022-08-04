package com.saving.metadata.utils;

import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.exception.ParamException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: 陈志强
 * @date: 2020/6/1 9:55
 * @Description: 效验字符串是否为空并进行抛出错误的工具类
 */
@Slf4j
public class StringUtils {

    private StringUtils() {
    }

    /**
     * 效验字符串是否为空并进行抛出错误
     *
     * @param maps maps key对应效验的参数,value对应需要抛出的错误提示信息
     */
    public static void checkStringIsBlankThrowError(Map<String, String> maps) {
        maps.forEach((key, value) -> {
            if (org.apache.commons.lang3.StringUtils.isBlank(key)) {
                log.error(value);
                throw new ParamException(value);
            }
        });
    }

    /**
     * 效验字符串是否为空并进行抛出错误
     *
     * @param param param 动态字符串数组  存放的参数为待效验的参数
     */
    public static void checkStringIsBlankThrowError(String... param) {
        List<String> strings = Arrays.asList(param);
        strings.forEach(str -> {
            if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
                log.error(str + "->" + ResponseCode.ERRORPARAM);
                throw new ParamException(ResponseCode.ERRORPARAM);
            }
        });
    }
}
