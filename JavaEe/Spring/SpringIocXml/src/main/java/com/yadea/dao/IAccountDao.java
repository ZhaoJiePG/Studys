package com.yadea.dao;

import com.yadea.domain.Account;

import java.util.List;

/**
 * 账户的持久层接口
 */
public interface IAccountDao {

    /**
     * 查询所有
     * @return
     */
    List<Account> findList();

    /**
     * 查询一个
     */
    Account findAccountById(Integer accountId);

    /**
     * 保存账户
     * @return
     */
    void saveAccount(Account account);

    /**
     * 更新
     */
    void updateAccount(Account account);

    /**
     * 删除
     */
    void deleteAccount(Integer accountId);
}
