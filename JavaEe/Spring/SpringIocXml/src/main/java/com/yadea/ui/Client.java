package com.yadea.ui;

import com.yadea.zj.service.IAccountService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 模拟一个表现层，用于调用业务层
 */
public class Client {

    /**
     * @param args
     */
    public static void main(String[] args) {
        //1.获取核心容器对象
//        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("bean1.xml");
        //2.根据id获取Bean对象
        IAccountService as  = (IAccountService)ac.getBean("accountService");
//        IAccountDao adao = ac.getBean("accountDao", IAccountDao.class);
//        System.out.println(as);
//        IAccountDao adao = ac.getBean("accountDao1",IAccountDao.class);
//        System.out.println(adao);
//        as.saveAccount();
        ac.close();
    }
}
