package com.yadea.zj.config;

import com.yadea.zj.service.HelloSerive;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ZJ on 2020/7/5
 * comment:@Configuration指明当前类是一个配置类：就是来替代spring的配置文件
 * 配置文件中使用bean标签吗
 * 配置类中使用@Bean
 */

@Configuration
public class MyAppConfig {

    //将方法的返回值添加到容器中，容器中的组件默认的id就是方法名称
    @Bean
    public HelloSerive helloSerive(){
        System.out.println("添加helloService成功");
        return new HelloSerive();
    }

}
