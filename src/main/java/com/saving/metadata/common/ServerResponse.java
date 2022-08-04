package com.saving.metadata.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;

import java.util.Map;


/**
 * @author Saving
 * @date 2018-4-20 17:23:09
 * 保证序列化json的时候,如果是null的对象,key也会消失
 */
public class ServerResponse<T> {

    /**
     * 状态
     */
    private final int status;
    /**
     * 信息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse<>(errorCode, errorMessage);
    }

    public static ServerResponse checkInsert(Integer result, String message) {
        return result >= 1 ? ServerResponse.createBySuccess() : ServerResponse.createByErrorMessage(message);
    }

    public static ServerResponse checkInsert(Integer[] result, String message) {
        for (Integer i : result) {
            if (i == 1) {
                ServerResponse.createBySuccess();
            }
        }
        return ServerResponse.createByErrorMessage(message);
    }

    /**
     * 使之不在json序列化结果当中
     */
    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
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
        maps.put("status", status);
        maps.put("msg", msg);
        maps.put("data", data);
        return maps;
    }


}
