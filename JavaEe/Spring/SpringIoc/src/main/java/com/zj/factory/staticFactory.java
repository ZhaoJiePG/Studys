package com.zj.factory;

import com.zj.service.IAccountService;
import com.zj.service.impl.AccountServiceImpl;

/**
 * Created by ZJ on 2020/5/27
 * comment:模拟一个工作类：该类可能存在于jar包中，我们无法提供默认构造函数
 */
public class staticFactory {

    public static IAccountService getAccountService(){
        return new AccountServiceImpl();
    }
}
