package dao;

import domain.Userr;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 *
 * 用户的持久层接口
 */
public interface IUserrDao {

    /**
     * 查询所有用户，同时获取到用户下所有账户的信息
     * @return
     */
    List<Userr> findAll();


    /**
     * 根据id查询用户信息
     * @param userId
     * @return
     */
    Userr findById(Integer userId);


}
