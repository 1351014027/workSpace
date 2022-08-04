package com.saving.metadata.common;

import com.saving.metadata.exception.ParamException;
import com.saving.metadata.exception.PermissionException;
import com.saving.metadata.utils.JsonMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 全局错误处理
 *
 * @author Mr.Saving
 * @date 2018-10-31 10:49
 **/
@Slf4j
@Component
public class SpringExceptionResolver implements HandlerExceptionResolver {

    private static final String JSONSUFFIX = ".json";
    private static final String PAGESUFFIX = ".page";

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        String url = httpServletRequest.getRequestURI();
        Map<? extends java.io.Serializable, ? extends String[]> parameterMap = httpServletRequest.getParameterMap();
        String viewName = "jsonView";
        ServerResponse jsonData;
        String errorMessage = "系统错误,请联系刷新页面或者联系管理员解决此问题";
        jsonData = ServerResponse.createByErrorMessage(errorMessage);
        if (url.endsWith(JSONSUFFIX)) {
            if (e instanceof PermissionException || e instanceof ParamException) {
                jsonData = ServerResponse.createByErrorMessage(e.getMessage());
            } else {
                log.error("未知的JSON请求错误,url:{},param:{}", url, JsonMapperUtil.object2String(parameterMap), e);
            }
        } else if (url.endsWith(PAGESUFFIX)) {
            log.error("未知的页面请求错误,url:{} ", url, e);
            viewName = "exception";
        } else {
            log.error("未知的请求错误,url:{},param:{}", url, JsonMapperUtil.object2String(parameterMap), e);
        }
        return new ModelAndView(viewName, jsonData.serverResponseData2Map());
    }
}
