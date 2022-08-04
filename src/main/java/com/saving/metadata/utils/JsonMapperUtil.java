package com.saving.metadata.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;

/**
 * 基于Jackson的JSON转换工具类
 *
 * @author Mr.Saving
 * @date 二〇一九年十二月十五日 22:51:29
 **/
@Slf4j
public class JsonMapperUtil {

    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

    static {

        // 对象的所有字段全部列入，还是其他的选项，可以忽略null等
        OBJECTMAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 设置Date类型的序列化及反序列化格式
        OBJECTMAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // 忽略空Bean转json的错误
        OBJECTMAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 忽略未知属性，防止json字符串中存在，java对象中不存在对应属性的情况出现错误
        OBJECTMAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 注册一个时间序列化及反序列化的处理模块，用于解决jdk8中localDateTime等的序列化问题
        OBJECTMAPPER.registerModule(new JavaTimeModule());
    }

    private JsonMapperUtil() {
    }

    public static <T> String object2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : OBJECTMAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("错误：对象转字符串转换异常！异常：{}", e);
            return null;
        }
    }

    public static <T> T string2Object(String str, TypeReference<T> typeReference) {
        if (str == null || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : OBJECTMAPPER.readValue(str, typeReference));
        } catch (Exception e) {
            log.error("错误：字符串转对象转换异常！异常：{}", e);
            return null;
        }
    }
}
