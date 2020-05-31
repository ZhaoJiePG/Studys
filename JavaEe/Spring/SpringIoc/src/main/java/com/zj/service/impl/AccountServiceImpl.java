package com.zj.service.impl;

import com.zj.dao.IAccountDao;
import com.zj.dao.impl.AccountDaoImpl;
import com.zj.service.IAccountService;

/**
 * 账户的业务层实现类
 */
public class AccountServiceImpl implements IAccountService {

    private IAccountDao accountDao = new AccountDaoImpl();

    public AccountServiceImpl(){
        System.out.println("对象创建了");
    }

    public void  saveAccount(){
        accountDao.saveAccount();
    }
    public void  distory(){
        System.out.println("容器销毁了");
    }
    public void  init(){
        System.out.println("容器初始化");
    }
}
