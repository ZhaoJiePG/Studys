package HelloWorld.dao;

import HelloWorld.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.util.List;

/**
 * Created by ZJ on 2020-4-20
 * comment:
 */
public class UserDaoImpl implements IUserDao{
    private SqlSessionFactory factory;

    public UserDaoImpl(SqlSessionFactory factory) {
        this.factory = factory;
    }

    public List<User> findAll() {
        //1.使用工厂创建sqlswssion对象
        SqlSession session = factory.openSession();
        //2.使用session执行查询所有方法
        List<User> users = session.selectList("HelloWorld.dao.IUserDao");
        session.close();
        //3.返回查询结果
        return users;
    }
}
