package com.saving.metadata.filter;


import com.google.common.collect.Lists;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.pojo.User;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

/**
 * @author Mr.Saving
 * @date 2018-10-31 17:33
 **/
@WebFilter(filterName = "loginDmsFilter", urlPatterns = {"/*"}, initParams = {@WebInitParam(name = "noLoginPaths", value = ".ttf;.woff;.html;.js;.gif;.jpg;.png;.css;.ico")})
@Order(1)
public class LoginDmsFilter implements Filter {

    private static final String NATUAL = "/login";
    private final List<String> noLoginPaths = Lists.newArrayList();
    @Value("${application.title}")
    private String webAppName;
    @Value("${application.enTitle}")
    private String webAppEnName;
    @Value("${application.titleCloud}")
    private String titleCloud;
    @Value("${application.enTitleCloud}")
    private String enTitleCloud;
    @Value("${application.isCloud}")
    private Boolean isCloud;

    @Override
    public void init(FilterConfig filterConfig) {
        //不做任何处理
        Collections.addAll(noLoginPaths, filterConfig.getInitParameter("noLoginPaths").split(";"));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        RequestHolder.add(request);
        request.getSession().setAttribute("webAppName", isCloud ? titleCloud : webAppName);
        request.getSession().setAttribute("webAppEnName", isCloud ? enTitleCloud : webAppEnName);
        request.getSession().setAttribute("isCloud", isCloud);
        if (request.getServletPath().contains(NATUAL)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        for (String noLoginPath : noLoginPaths) {
            if (request.getServletPath().endsWith(noLoginPath)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        if (request.getServletPath().contains("/metadata/metaDataTables/syncAllData.json") || request.getServletPath().contains("/metadata/metaDataTables/syncMaxVersion.json")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (request.getServletPath().contains("/metadata/revisionlog/save.json")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (request.getServletPath().contains("/metadata/ApiImplements")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (request.getServletPath().contains("/metadata/fileStore/listPage.json")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (request.getServletPath().contains("swagger")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        User user = (User) request.getSession().getAttribute(ResponseCode.CURRENT_USER);

        if (user == null) {
            if (request.getServletPath().endsWith("json")) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter out = response.getWriter();
                JSONObject res = new JSONObject();
                res.put("msg", ResponseCode.NEED_LOGIN.getDesc());
                res.put("status", ResponseCode.NEED_LOGIN.getCode());
                out.append(res.toString());
                return;
            } else {
                response.sendRedirect(request.getContextPath() + NATUAL + ".page");
                return;
            }
        }
        RequestHolder.add(user);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        //不做任何处理
    }

}
