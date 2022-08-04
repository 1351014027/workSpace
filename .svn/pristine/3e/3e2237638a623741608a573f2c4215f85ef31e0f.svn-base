package com.saving.metadata.interceptor;

/**
 * @author: 陈志强
 * @date: 2020/4/15 9:56
 * @Description:
 */

import com.saving.metadata.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;


/**
 * @author: 陈志强
 * @date: 二〇二〇年五月十五日 14:27:52
 * @Description: 全局文件类型拦截器
 */
@Slf4j
@Component
public class FileTypeInterceptor extends HandlerInterceptorAdapter {

    @Value("${application.fileType}")
    private String fileType;


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        boolean flag = true;
        // 判断是否为文件上传请求
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest =
                    (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> files =
                    multipartRequest.getFileMap();
            Iterator<String> iterator = files.keySet().iterator();
            //对多部件请求资源进行遍历
            while (iterator.hasNext()) {
                String formKey = iterator.next();
                MultipartFile multipartFile =
                        multipartRequest.getFile(formKey);
                String filename = multipartFile.getOriginalFilename();
                //判断是否为限制文件类型
                if (!checkFile(filename)) {
                    //限制文件类型，请求转发到原始请求页面，并携带错误提示信息
                    flag = false;
                    throw new ParamException("文件类型只允许：xlsx,xls,doc,docx,ppt,pptx,pdf,rar,zip,jpg,png,jpeg");

                }
            }
        }
        return flag;
    }

    /**
     * 判断是否为允许的上传文件类型,true表示允许
     */
    private boolean checkFile(String fileName) {
        //设置允许上传文件类型
//        String suffixList = "xlsx,xls,doc,docx,ppt,pptx,pdf,rar,zip,jpg,png,jpeg";
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")
                + 1);
        return fileType.contains(suffix.trim().toLowerCase());
    }
}
