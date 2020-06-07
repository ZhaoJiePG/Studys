package com.zj.service.impl;

import com.zj.dao.IAccountDao;
import com.zj.domain.Account;
import com.zj.service.IAccountService;

import java.util.List;

/**
 * 账户的业务层实现类
 *
 * 曾经XML的配置：
 *  <bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl"
 *        scope=""  init-method="" destroy-method="">
 *      <property name=""  value="" | ref=""></property>
 *  </bean>
 *
 *
 */

//@Service("accountService")
//@Scope("prototype")
public class AccountServiceImpl implements IAccountService {
    private IAccountDao accountDao;

    public void setAccountDao(IAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public List<Account> findList() {
        return accountDao.findList();
    }

    public Account findAccountById(Integer accountId) {
        return null;
    }

    public void saveAccount(Account account) {
        accountDao.saveAccount(account);
    }

    public void updateAccount(Account account) {
        accountDao.updateAccount(account);
    }

    public void deleteAccount(Integer accountId) {
        accountDao.deleteAccount(accountId);
    }
}
