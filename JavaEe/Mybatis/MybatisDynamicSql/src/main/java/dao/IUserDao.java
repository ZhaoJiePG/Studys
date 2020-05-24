package dao;

import domain.QueryVo;
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
     * @Description:更具QueryVo查询中的条件查询用户
     * @Param: user
     * @return:
     */
    List<User> findUserByVo(QueryVo vo);

    /**
     * @Description:更具传入参数条件
     * @Param: user 条件查询，可能有姓名，可能都没有
     * @return:
     */
    List<User> findUserByCondition(User user);

    /**
     * @Description:更具Queryvo中的id集合查询中的条件查询用户
     * @Param: user 条件查询，可能有姓名，可能都没有
     * @return:
     */
    List<User> findUserInIds(QueryVo vo);
}
