package com.yadea;

import com.yadea.bean.Person;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * SpringBoot单元测试
 * 可以在测试期间很方便的类似编码一样的进行自动注入等容器的操作
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class QuickspringbootApplicationTests {

    @Autowired
    ApplicationContext ioc;

    @Autowired
    Person person;

    //记录器
    Logger logger = LoggerFactory.getLogger(getClass());

    //测试日志
    @Test
    public void testLogging(){
        /**
         * 日志级别：由低到高，可以调整日志级别
         * springboot默认是info级别的
         */
        //跟踪轨迹日志
        logger.trace("这是trace日志。。。。");
        //调试信息
        logger.debug("这是debug日志。。。");
        //自定义信息
        logger.info("这是info日志。。。");
        //警告日志
        logger.warn("这是warn日志。。。");
        //error日志
        logger.error("这是error日志。。。");
    }

    @Test
    public void testHellWorld(){
        boolean containsBean = ioc.containsBean("helloSerive");
        System.out.println(containsBean);
    }

    @Test
    void contextLoads() {
        System.out.println(person);
    }
}
