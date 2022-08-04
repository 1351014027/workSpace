package com.saving.metadata.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.saving.metadata.utils.MD5Util;
import com.saving.metadata.utils.PasswordUtil;

/**
 * 常用参数对象
 *
 * @author Saving
 * @date 2019年4月3日14:49:00
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum ResponseCode {

    /**
     * 成功  状态码为0
     */
    SUCCESS(0, "SUCCESS"),
    /**
     * 失败  状态码为1
     */
    ERROR(1, "ERROR"),
    /**
     * 未登录  状态码为10
     */
    NEED_LOGIN(10, "NEED_LOGIN"),
    /**
     * 非法参数  状态码为2
     */
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT");

    public static final String PAGENUM = "1";
    public static final String PAGESIZE = "100";
    public static final String ERRORPARAM = "错误的对象参数!";
    public static final String ERRORPPERSSION = "非法操作!";
    public static final String UPDATEERRORMSG = "待更新的记录不存在!";
    public static final String MOOD = "已经存在，请勿添加相同记录!";
    public static final String NORECORD = "查无此记录!";
    public static final String CZCSTIME = "操作超时请重新校验!";
    public static final String THEREFERENCE = "存在引用该记录，无法删除!";
    public static final String ADMINISTRATOR = "超级管理员";
    public static final String CURRENT_USER = "currentuser";
    public static final String ANONYMITY = "anonymity";
    public static final String AUTHOR = "saving";
    public static final String FILE = "file";
    public static final String TEMPLATE = "template";
    public static final String LINK = "link";
    public static final String EXPERTDOCUMENTEN = "expertDocumenten";
    public static final String EXOERTFILE = "exoertFile";
    public static final String AUTHOR_MM = MD5Util.createMd5(PasswordUtil.WORD[0] + PasswordUtil.WORD[8] + PasswordUtil.WORD[2] + "@" + 1 + PasswordUtil.NUM[1] + 10);
    private final int code;
    private final String desc;


    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


}
