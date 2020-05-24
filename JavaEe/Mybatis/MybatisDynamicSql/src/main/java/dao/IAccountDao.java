package dao;

import domain.Account;
import domain.AccountUser;

import java.util.List;

/**
 * Created by ZJ on 2020/5/24
 * comment:
 */
public interface IAccountDao {

    /**
     * 查询所有账户
     */
    List<Account> findAll();

    /**
     * 查询所有账户并且带有账户的用户信息
     */
    List<AccountUser> findAllAccount();
}
