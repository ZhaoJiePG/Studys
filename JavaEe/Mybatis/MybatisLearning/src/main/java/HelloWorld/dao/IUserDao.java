package HelloWorld.dao;

import HelloWorld.domain.User;

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
}
