package com.saving.metadata.common;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * sav-sys
 * 用于获取上下文的Bean
 *
 * @author Mr.Saving
 * @date 2019年4月3日14:47:40
 **/
@Component("applicationContextHolder")
public class ApplicationContextHolder implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public <T> T popBean(Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    public <T> T popBean(String name, Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name, clazz);
    }
}
