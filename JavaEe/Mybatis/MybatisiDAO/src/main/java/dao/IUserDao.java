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
    void saveUser(User user);

    /**
     * @Description:更新操作
     * @Param: user
     * @return:
     */
    void updateUser(User user);

    /**
     * @Description:删除操作
     * @Param: user
     * @return:
     */
    void deleteUser(int id);

    /**
     * @Description:根据id查询
     * @Param: user
     * @return:
     */
    User findUserById(int userId);

    /**
     * @Description:根据名称模糊查询
     * @Param: user
     * @return:
     */
    List<User> findUserByName(String userName);

    /**
     * @Description:查询总的用户数
     * @Param: user
     * @return:
     */
    int findTotal();

}
