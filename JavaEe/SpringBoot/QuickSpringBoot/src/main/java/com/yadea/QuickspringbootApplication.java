package com.yadea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@ImportResource导入指定的spring的配置文件，让其生效
//@ImportResource(locations = {"classpath:beans.xml"})
@SpringBootApplication
public class QuickspringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickspringbootApplication.class, args);
    }

}
