package com.zj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

import static springfox.documentation.service.ApiInfo.DEFAULT_CONTACT;

/**
 * Created by ZJ on 2020/8/6
 * comment:
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    //配置swagger的bean实例
    @Autowired
    public Docket docket(Environment environment){

        //获取项目的环境,设置要显示的swagger环境
//        String property = environment.getProperty("spring.profiles.active");
//        if (property == "dev"){
//
//        }

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true)
                .select()
                //basePackage指定扫描的包
                //any()全部 none()都不扫描
                //withClassAn扫描类上的注解，参数是一个注解的反射对象
                //WithMethmodAn扫描方法上的注解
                .apis(RequestHandlerSelectors.basePackage("com.zj"))
                //过滤路径
                .paths(PathSelectors.any())
                .build();
    }

    //配置swagger信息ApiInfo类
    public ApiInfo apiInfo(){
        //作者信息
        Contact contact = new Contact("zj", "http://127.0.0.1:8080/swagger-ui.html#/", "qq.com");

        return new ApiInfo("ZJ-Swagger-Api文档",
                "Api-Documentation",
                "1.0",
                "urn:tos",
                contact,
                "Apache 2.0",
                "http://127.0.0.1:8080/swagger-ui.html#/",
                new ArrayList());
    }


}
