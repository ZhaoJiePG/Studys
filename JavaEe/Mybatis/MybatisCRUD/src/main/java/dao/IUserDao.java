package dao;

import domain.User;

import java.util.List;

/**
 * Created by ZJ on 2020-4-19
 * comment: 用户持久层接口
 */
public interface IUserDao {
    /**
     * 查询所有操作
     * @return
     */
    List<User> findAll();
    /**
     * 保存操作
     * @return
     * @param user
     */
    List<User> saveUser(User user);
}
