package com.saving.metadata.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author:
 * @date: 2019/12/12 14:22
 * @Description: 生成接口文档配置类
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class SwaggerConfig {


    @Value("${application.author}")
    private String author;
    @Value("${application.title}")
    private String title;
    @Value("${application.version}")
    private String version;


    //配置swagger的Docket的bean实例
    @Bean
    public Docket productApi(Environment environment) {

        //设置要显示的swagger环境
        //Profiles profiles = Profiles.of("dev","prod");

        //获取项目的环境
        //boolean flag  = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2).select()
                // 扫描的包路径
                // any();扫描全部
                // none();不扫描
                // withClassAnnotation;扫描类上的注解，参数是一个注解的反射对象
                // withMethodAnnotation;扫描方法上的注解
                .apis(RequestHandlerSelectors.basePackage("com.saving.metadata.controller"))
                // 定义要生成文档的Api的url路径规则
                // ant();扫描固定路径
                .paths(PathSelectors.any()).build()
                // 设置swagger-ui.html页面上的一些元素信息。
                //.enable(flag) //是否启用swagger,true为可以，false为不行
                //分组，配置多个Docket即可实现多个分组
                //.groupName("A")
                .apiInfo(metaData());
    }

    //配置swagger信息
    private ApiInfo metaData() {
        //作者信息
        return new ApiInfoBuilder()
                // 标题
                .title(title)
                // 描述
                .description(title + "API接口文档")
                // 文档版本
                .version(version).build();
    }



}
