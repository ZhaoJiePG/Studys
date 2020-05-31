package com.zj.ui;

import com.zj.service.IAccountService;
import com.zj.service.impl.AccountServiceImpl3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 模拟一个表现层，用于调用业务层
 */
public class Client3 {
    /**
     *
     */
    public static void main(String[] args) {
        //1.获取核心容器对象
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean3.xml");
        //2.根据id获取bean对象
        IAccountService as = (AccountServiceImpl3) ac.getBean("accountService");
        System.out.println(as);
        as.saveAccount();
    }
}
