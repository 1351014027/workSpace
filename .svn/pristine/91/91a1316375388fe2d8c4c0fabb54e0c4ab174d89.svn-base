package com.saving.metadata.interceptor;

import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.utils.JsonMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 接口返回速度拦截器，并每次访问后清除当前线程中的对象
 *
 * @author Mr.Saving
 * @date 2019年4月3日14:53:50
 **/
@Slf4j
@Component
public class HttpInterceptor extends HandlerInterceptorAdapter {

    private static final String STATRE_TIME = "requestStartTime";
    private static final String JSONSUFFIX = ".json";
    private static final String PAGESUFFIX = ".page";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String url = request.getRequestURI();
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (url.endsWith(JSONSUFFIX) || url.endsWith(PAGESUFFIX)) {
            log.info("请求开始.usl:{},param:{}", url, JsonMapperUtil.object2String(parameterMap));
            long l = System.currentTimeMillis();
            request.setAttribute(STATRE_TIME, l);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        removeThreadLocalInfo();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getRequestURI();
        if (url.endsWith(JSONSUFFIX) || url.endsWith(PAGESUFFIX)) {
            long stateTime = (long) request.getAttribute(STATRE_TIME);
            long endTime = System.currentTimeMillis();
            log.info("请求完成!url:{},耗时(毫秒)：{}", url, endTime - stateTime);
        }
        removeThreadLocalInfo();
    }

    public void removeThreadLocalInfo() {
        RequestHolder.remove();
    }
}
