package com.zj;

import com.zj.bean.Person;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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

    @Test
    public void testHellWorld(){
        boolean containsBean = ioc.containsBean("helloSerive");
        System.out.println(containsBean);
    }

    @Autowired
    Person person;

    @Test
    void contextLoads() {
        System.out.println(person);
    }

}
