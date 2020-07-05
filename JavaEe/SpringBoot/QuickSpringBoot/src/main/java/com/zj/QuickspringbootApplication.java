package com.zj;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

//@ImportResource导入指定的spring的配置文件，让其生效
//@ImportResource(locations = {"classpath:beans.xml"})
@SpringBootApplication
public class QuickspringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickspringbootApplication.class, args);
    }

}
