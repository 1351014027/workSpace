package com.saving.metadata.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.exception.PermissionException;
import com.saving.metadata.pojo.SchooleCode;
import com.saving.metadata.service.SchooleCodeService;
import com.saving.metadata.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

/**
 * 接口返回速度拦截器，并每次访问后清除当前线程中的对象
 *
 * @author Mr.Saving
 * @date 2019年4月3日14:53:50
 **/
@Slf4j
@Component
public class SyncInterceptor extends HandlerInterceptorAdapter {

    private final SchooleCodeService schooleCodeService;

    private final String SYNCCOUNTURL = "/metadata/metaDataTables/syncMaxVersion.json";
    private final String SYNCDATAURL = "/metadata/metaDataTables/syncAllData.json";

    @Autowired
    public SyncInterceptor(SchooleCodeService schooleCodeService) {
        this.schooleCodeService = schooleCodeService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String url = request.getRequestURI();
        if (url.contains(SYNCCOUNTURL) || url.contains(SYNCDATAURL)) {
            String apiKey = request.getParameter("apiKey");
            String schoolCode = request.getParameter("schoolCode");
            if (StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(schoolCode)) {
                throw new ParamException(ResponseCode.ERRORPARAM);
            }
            SchooleCode code = schooleCodeService.getOne(new QueryWrapper<SchooleCode>()
                    .eq("SchoolCode_", schoolCode)
                    .eq("ApiKey_", apiKey)
                    .last("and getdate() between StartTime_ and EndTime_"));
            if (code == null) {
                throw new PermissionException("该学校暂无授权，请授权后重试！");
            }
            SchooleCode schooleCode = SchooleCode.builder()
                    .id(code.getId())
                    .lastupdateip(IpUtils.getIpAddr(request))
                    .lastupdatetime(Calendar.getInstance().getTime())
                    .lastupdatesize(code.getLastupdatesize() + 1).build();
            schooleCodeService.updateById(schooleCode);
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        removeThreadLocalInfo();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

    public void removeThreadLocalInfo() {
        RequestHolder.remove();
    }
}
