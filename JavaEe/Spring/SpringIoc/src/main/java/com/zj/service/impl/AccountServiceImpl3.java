package com.zj.service.impl;

import com.zj.dao.IAccountDao;
import com.zj.dao.impl.AccountDaoImpl;
import com.zj.service.IAccountService;

import java.util.Date;

/**
 * 账户的业务层实现类
 */
public class AccountServiceImpl3 implements IAccountService {

    //如果数据经常变化，并不适用于注入的方式
    private String name;
    private Integer age;
    private Date birthday;

    public AccountServiceImpl3(String name, Integer age, Date birthday) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }

    public void saveAccount() {
        System.out.println("service中的saveAccount方法执行了。。。"+name+','+age+','+birthday);
    }
}
