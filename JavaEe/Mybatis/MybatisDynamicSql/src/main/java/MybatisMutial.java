import utils.IAccountDao;
import utils.IUserDao;
import domain.Account;
import domain.AccountUser;
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
import java.util.List;

/**
 * Created by ZJ on 2020-4-19
 * comment:
 */
public class MybatisMutial {

    private InputStream in;
    private SqlSession sqlSession;
    private IUserDao userDao;
    private IAccountDao accountDao;

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
        accountDao = sqlSession.getMapper(IAccountDao.class);
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
        List<Account> accounts = accountDao.findAll();
        for(Account account: accounts){
            System.out.println(account);
        }
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
     *查询所有账户包含用户名称（1对1）
     */
    @Test
    public void testFindAllAccount(){
        //执行查询方法
        List<AccountUser> user = accountDao.findAllAccount();
        for(AccountUser account: user){
            System.out.println(account);
        }
    }

    /**
     *查询所有账户包含用户名称（1对多）
     */
    @Test
    public void testFindAll2(){
        //执行查询方法
        List<User> user = userDao.findAll();
        List<User> user2 = userDao.findAll();
        System.out.println(user == user2);
        for(User users: user){
            System.out.println("================");
            System.out.println(users);
//            System.out.println(users.getAccounts());
        }
    }


}
