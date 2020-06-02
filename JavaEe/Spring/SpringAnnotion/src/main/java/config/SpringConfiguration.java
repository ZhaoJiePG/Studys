package config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * Created by ZJ on 2020/6/2
 * comment:该配置类作用和bean.xml是一样
 * spring中的新注解
 * Configuration
 *      作用：指定当前类是一个配置类
 * ComponentScan
 *      作用：用于通过注解指定spring在创建容器时要扫描的包
 *      属性：
 *          value：它和basePacksges的作用一样，都是用于指定创建容器时要扫描的包
 * Bean注解：
 *      作用：用于把当前方法的返回值作为bean对象存入容器中
 *      属性：
 *          name：用于指定bean的id（不写时，默认时当前方法的名称）
 *      细节：
 *         当我们使用注解配置方法时，如果方法有参数，spring框架回去容器中查找有没有可用的bean框架。
 *         查找的方式和Autowried注解的作用时一样的
 */
@Configuration
@ComponentScan(basePackages = {"com.zj"})
public class SpringConfiguration {

    /* *
     * @Description: 用于创建一个queryRunner类
     * @Param: [dataSource] 
     * @return: org.apache.commons.dbutils.QueryRunner 
     */
    @Bean(name = "runner")
    public QueryRunner createQueryRunner(DataSource dataSource){
        return new QueryRunner(dataSource);
    }

    @Bean(name = "dataSource")
    public DataSource createDataSource() throws PropertyVetoException {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass("com.mysql.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql:/hadoop01:3306/mybatis_test");
        ds.setUser("root");
        ds.setPassword("123456");
        return ds;
    }
}
