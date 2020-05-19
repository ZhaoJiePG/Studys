import dao.IUserDao;
import domain.QueryVo;
import domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by ZJ on 2020-4-19
 * comment:
 */
public class MybatisCRUD {

    private InputStream in;
    private SqlSession sqlSession;
    private IUserDao userDao;

    @Before//用于在测试方法执行之前执行
    public void init()throws Exception{
        //1.读取配置文件，生成字节输入流
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2.获取SqlSessionFactory
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        //3.获取SqlSession对象
        sqlSession = factory.openSession();
        //4.获取dao的代理对象
        userDao = sqlSession.getMapper(IUserDao.class);
    }

    @After//用于在测试方法执行之后执行
    public void destroy()throws Exception{
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
        in.close();
    }

    /**
     * 测试查询所有
     */
    @Test
    public void testFindAll() throws IOException {
        List<User> users = userDao.findAll();
        for(User user: users){
            System.out.println(user);
        }
    }

    /**
     * 保存用户
     */
    @Test
    public void testAddUser(){
        User user = new User();
        user.setUserName("zhaojie");
        user.setUserAddress("江苏省无锡市");
        user.setUserSex("男");
        user.setUserBirthday(new Date());
        System.out.println("保存之前"+user);
        //执行保存
        userDao.saveUser(user);
        System.out.println("保存之后"+user);
    }

    /**
     *测试更新操作
     */
    @Test
    public void testUpdate(){
        User user = new User();
        user.setUserId(50);
        user.setUserName("mybatis_test");
        user.setUserAddress("江苏省无锡市");
        user.setUserSex("男");
        user.setUserBirthday(new Date());

        //执行更新方法
        userDao.updateUser(user);
    }

    /**
     *测试删除操作
     */
    @Test
    public void testDelete(){
        User user = new User();
        user.setUserId(50);
        user.setUserName("mybatis_test");
        user.setUserAddress("江苏省无锡市");
        user.setUserSex("男");
        user.setUserBirthday(new Date());

        //执行更新方法
        userDao.deleteUser(42);
    }

    /**
     *测试查询方法
     */
    @Test
    public void testFindOne(){
        //执行查询方法
        User user = userDao.findUserById(41);
        System.out.println(user);
    }

    /**
     *测试模糊查询方法
     */
    @Test
    public void testFindByName(){
        //执行查询方法
//        List<User> users = userDao.findUserByName("%王%");
        List<User> users = userDao.findUserByName("王");
        for (User user : users){
            System.out.println(user);
        }
    }

    /**
     *查询总数
     */
    @Test
    public void findTotal(){
        //执行查询方法
        int all = userDao.findTotal();
        System.out.println(all);
    }

    /**
     *更具QueryVo查询中的条件查询用户
     */
    @Test
    public void findUserByVo(){
        //执行查询方法
        QueryVo vo = new QueryVo();
        User user = new User();
        user.setUserName("%王%");
        List<User> userByVo = userDao.findUserByVo(vo);
        for(User user1 : userByVo){
            System.out.println(user1);
        }
    }


}
