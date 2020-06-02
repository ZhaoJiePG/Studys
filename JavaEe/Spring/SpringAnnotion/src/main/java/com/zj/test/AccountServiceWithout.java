package com.zj.test;

import com.zj.domain.Account;
import com.zj.service.IAccountService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by ZJ on 2020/6/1
 * comment:使用Junit单元测试配置
 */
public class AccountServiceWithout {

    ApplicationContext ac = null;
    IAccountService as = null;

    @Before
    public void init(){
        ac = new ClassPathXmlApplicationContext("beanIoc.xml");
        //根据id获取Bean对象
        as  = (IAccountService)ac.getBean("accountService");

    }

    @Test
    public void findList(){
        List<Account> accounts = as.findList();
        for(Account account:accounts){
            System.out.println(account);
        }
    }

    @Test
    public void findAccountById(){
        Account accountById = as.findAccountById(new Integer(1));
        System.out.println(accountById);
    }

    @Test
    public void saveAccount(){
        Account a = new Account();
        a.setId(4);
        a.setName("zj");
        a.setMoney(2000.0);
        as.saveAccount(a);
    }

    @Test
    public void updateAccount(){
        Account a = new Account();
        a.setId(4);
        a.setName("zj");
        a.setMoney(2000.0);
        as.updateAccount(a);
    }

    @Test
    public void deleteAccount(){
        as.deleteAccount(1);
    }


}
