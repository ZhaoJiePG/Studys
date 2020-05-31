package com.zj.ui;

import com.zj.dao.IAccountDao;
import com.zj.service.IAccountService;
import com.zj.service.impl.AccountServiceImpl;
import javafx.application.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 模拟一个表现层，用于调用业务层
 */
public class Client {
    /**
     * 获取springioc的核心容器并根据id获取对象
     *
     * ApplicationContext的三个常用实现类：
     *      ClassPathXmlApplicationContext:它可以加载类路径下的配置文件，要求配置文件必须在类路径下
     *      FileSystemXmlApplicationContext:它可以加载磁盘上任意路径下的配置文件
     *      AnnotionConfApplication“它是用于读取注解创建容器的
     *
     * 核心容器的两个接口引发的问腿：
     *      ApplicatioContext：
     *          它在创建核心容器是，创建对象的策略是立即加载，一读取完马上配置配置文件中的对象
     *      BeanFactory：
     *          它在构建核心容器时，创建对象采用延迟加载的方式，什么时候根据id获取对象了，才真正创建对象
     */
    public static void main(String[] args) {
        //1.获取核心容器对象
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        //2.根据id获取bean对象
        IAccountService as = (IAccountService) ac.getBean("accountService");
        IAccountDao ao = ac.getBean("accountDao", IAccountDao.class);

        System.out.println(as);
        as.saveAccount();
        System.out.println(ao);

        //第二种方式
        ApplicationContext ac2 = new FileSystemXmlApplicationContext("D:\\Maven\\Studys\\JavaEe\\Spring\\SpringIoc\\src\\main\\resources\\bean.xml");
        //2.根据id获取bean对象
        IAccountService as2 = (IAccountService) ac.getBean("accountService");
        IAccountDao ao2 = ac.getBean("accountDao", IAccountDao.class);

        System.out.println(as2);
        System.out.println(ao2);
    }
}
