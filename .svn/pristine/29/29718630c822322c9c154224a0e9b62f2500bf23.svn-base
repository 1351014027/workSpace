package com.saving.metadata;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author Administrator
 */
@SpringBootApplication
@MapperScan("com.saving.metadata.dao")
//@ComponentScan("com.saving.metadata.config")
//@ComponentScan("com.saving.metadata.interceptor")
@ServletComponentScan
@EnableCaching  //spring framework中的注解驱动的缓存管理功能
public class MetadataApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(MetadataApplication.class, args);
    }
}
