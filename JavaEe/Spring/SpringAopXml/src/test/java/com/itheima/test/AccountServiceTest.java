package com.itheima.test;

import com.itheima.service.IAccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 使用Junit单元测试：测试我们的配置
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bean.xml")
public class AccountServiceTest {

//    @Autowired
//    @Qualifier("proxyAccountService")
//    private  IAccountService as;
    ApplicationContext ac = null;
    IAccountService as = null;

    @Before
    public void init(){
        ac = new ClassPathXmlApplicationContext("bean.xml");
        //根据id获取Bean对象
        as  = (IAccountService)ac.getBean("accountService");

    }
    @Test
    public  void testTransfer(){
        as.transfer("aaa","bbb",100f);
    }

}
