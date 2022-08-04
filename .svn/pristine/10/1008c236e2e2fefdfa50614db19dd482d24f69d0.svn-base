package com.saving.metadata.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;

import java.util.Map;


/**
 * 构建树形结构返回对象
 *
 * @author Saving
 * @date 2019年4月3日14:48:39
 * 保证序列化json的时候,如果是null的对象,key也会消失
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LayuiTreeResult<T> {

    /**
     * 状态
     */
    private final int code;
    /**
     * 信息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;

    private LayuiTreeResult(int code) {
        this.code = code;
    }

    private LayuiTreeResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    private LayuiTreeResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private LayuiTreeResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> LayuiTreeResult<T> createBySuccess() {
        return new LayuiTreeResult<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> LayuiTreeResult<T> createBySuccessMessage(String msg) {
        return new LayuiTreeResult<>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> LayuiTreeResult<T> createBySuccess(T data) {
        return new LayuiTreeResult<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> LayuiTreeResult<T> createBySuccess(String msg, T data) {
        return new LayuiTreeResult<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> LayuiTreeResult<T> createByError() {
        return new LayuiTreeResult<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> LayuiTreeResult<T> createByErrorMessage(String errorMessage) {
        return new LayuiTreeResult<>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> LayuiTreeResult<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new LayuiTreeResult<>(errorCode, errorMessage);
    }

    public static LayuiTreeResult checkInsert(Integer result, String message) {
        return result >= 1 ? LayuiTreeResult.createBySuccess() : LayuiTreeResult.createByErrorMessage(message);
    }

    public static LayuiTreeResult checkInsert(Integer[] result, String message) {
        for (Integer i : result) {
            if (i == 1) {
                LayuiTreeResult.createBySuccess();
            }
        }
        return LayuiTreeResult.createByErrorMessage(message);
    }

    /**
     * 使之不在json序列化结果当中
     */
    @JsonIgnore
    public boolean isSuccess() {
        return this.code == ResponseCode.SUCCESS.getCode();
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 使之不在json序列化结果当中
     */
    @JsonIgnore
    public Map<String, Object> serverResponseData2Map() {
        Map<String, Object> maps = Maps.newHashMap();
        maps.put("code", code);
        maps.put("msg", msg);
        maps.put("data", data);
        return maps;
    }


}
