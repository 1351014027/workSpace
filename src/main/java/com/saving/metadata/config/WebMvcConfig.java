package com.saving.metadata.config;

import com.saving.metadata.interceptor.FileTypeInterceptor;
import com.saving.metadata.interceptor.HttpInterceptor;
import com.saving.metadata.interceptor.RoleInterceptor;
import com.saving.metadata.interceptor.SyncInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author: 陈志强
 * @date: 2020/5/22 15:44
 * @Description:
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {


    private final HttpInterceptor httpInterceptor;
    private final SyncInterceptor syncInterceptor;
    private final FileTypeInterceptor fileTypeInterceptor;
    private final RoleInterceptor roleInterceptor;

    @Autowired
    public WebMvcConfig(HttpInterceptor httpInterceptor, SyncInterceptor syncInterceptor, FileTypeInterceptor fileTypeInterceptor, RoleInterceptor roleInterceptor) {
        this.httpInterceptor = httpInterceptor;
        this.syncInterceptor = syncInterceptor;
        this.fileTypeInterceptor = fileTypeInterceptor;
        this.roleInterceptor = roleInterceptor;
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
        addDefaultHttpMessageConverters(converters);
    }

    @Override
    protected void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/swagger/**").addResourceLocations("classpath:/static/swagger/");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpInterceptor).addPathPatterns("/**").excludePathPatterns("/");
        registry.addInterceptor(syncInterceptor).addPathPatterns("/**").excludePathPatterns("/");
        registry.addInterceptor(roleInterceptor).addPathPatterns("/**").excludePathPatterns("/");
        registry.addInterceptor(fileTypeInterceptor)
                .addPathPatterns("/metadata/*/import.json")
                .addPathPatterns("/metadata/upload/uploadFile.json")
                .addPathPatterns("/metadata/upload/uploadFiles.json")
                .excludePathPatterns("/**/specification.pdf", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/*.html", "/**/*.html", "/swagger-resources/**"); /*swagger接口文档过滤   "/swagger-ui.html/**"   */
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 是否允许证书 不再默认开启
                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("*")
                .allowedHeaders(
                        "X-PINGOTHER",
                        "Content-Type",
                        "X-Requested-With",
                        "accept",
                        "Origin",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers",
                        "Authorization",
                        "x-xsrf-token")
                // 跨域允许时间
                .maxAge(3600);
    }
}
