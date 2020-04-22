package HelloWorld;

import HelloWorld.dao.IUserDao;
import HelloWorld.dao.IUserDao2;
import HelloWorld.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by ZJ on 2020-4-19
 * comment:
 */
public class MybatisAnnotation {
    /**
     * 入门案列 
     * @param args
     */
    public static void main(String[] args) throws IOException {
        //1.读取配置文件
        InputStream in = new FileInputStream("D:\\Maven\\Studys\\JavaEe\\Mybatis\\MybatisLearning\\src\\main\\java\\HelloWorld\\SqlMapConfig2.xml");
        //2.创建SqlSessionFactory
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        //3.使用工厂生产SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //4.使用SqlSession创建Dao的代理对象
        IUserDao2 userDao = sqlSession.getMapper(IUserDao2.class);
        //5.使用代理对象执行方法
        List<User> users = userDao.findAll();
        for(User user: users){
            System.out.println(user);
        }
        //6.释放资源
        sqlSession.close();
        in.close();
    }
}
