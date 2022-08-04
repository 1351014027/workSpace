package com.saving.metadata.common;


import com.saving.metadata.pojo.User;

import javax.servlet.http.HttpServletRequest;


/**
 * 用于替代接口每次都要写session参数，直接从当前线程中获取
 *
 * @author Mr.Saving
 * @date 2019年4月3日14:46:55
 **/
public class RequestHolder {
    private static final ThreadLocal<User> USERHOLDER = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletRequest> USERREQUEST = new ThreadLocal<>();

    private RequestHolder() {

    }

    public static void add(User user) {
        USERHOLDER.set(user);
    }

    public static void add(HttpServletRequest request) {
        USERREQUEST.set(request);
    }

    public static User getCurrenSysUser() {
        if (USERHOLDER.get() == null) {
            return null;
        }
        return USERHOLDER.get();
    }

    public static HttpServletRequest getCurrenRequest() {
        if (USERREQUEST.get() == null) {
            return null;
        }
        return USERREQUEST.get();
    }

    public static void remove() {
        USERHOLDER.remove();
        USERREQUEST.remove();
    }
}
