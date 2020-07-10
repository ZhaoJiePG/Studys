package com.zj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * Created by ZJ on 2020/7/7
 * comment:
 */

@SpringBootApplication
public class SpringBootRestfulApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootRestfulApp.class, args);
    }

    @Bean
    public ViewResolver myViewResolver(){
        return new MyViewResolver();
    }

    private static class MyViewResolver implements ViewResolver{

        public View resolveViewName(String s, Locale locale) throws Exception {
            return null;
        }
    }
}

