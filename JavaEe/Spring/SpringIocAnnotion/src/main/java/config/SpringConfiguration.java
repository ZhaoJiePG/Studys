package config;

import org.springframework.context.annotation.*;

/**
 * Created by ZJ on 2020/6/2
 * comment:该配置类作用和bean.xml是一样
 * spring中的新注解
 * Configuration
 *      作用：指定当前类是一个配置类
 *      细节：当配置类作为annotationConfig对象创建的参数时，该注解可以不写
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
 * Import注解：
 *      作用：用于导入其他配置类
 *      属性：
 *          value：用于指定其它配置类的字节码
 *                  当我们谁用Import的注解之后，有Import注解的类就是父配置类，导入的类为子配置类
 *
 * PropertySopurce:
 *      作用：用于指定properties文件的位置
 *      属性：
 *          value：指定文件的名称和文件的路径
 *          关键字：classpath：表示类路径下
 */
//@Configuration
@ComponentScan(basePackages = {"com.yadea"})
@Import(JDBCConfig.class)
@PropertySource("classpath:jdbcconfig.properties")
//@ComponentScan(basePackages = {"com.zj","com.config"})
public class SpringConfiguration {

    /* *
     * @Description: 用于创建一个queryRunner类
     * @Param: [dataSource]
     * @return: org.apache.commons.dbutils.QueryRunner
     */
//    @Bean(name = "runner")
//    @Scope("prototype")
//    public QueryRunner createQueryRunner(DataSource dataSource){
//        return new QueryRunner(dataSource);
//    }
//
//    @Bean(name = "dataSource")
//    public DataSource createDataSource() throws PropertyVetoException {
//        ComboPooledDataSource ds = new ComboPooledDataSource();
//        ds.setDriverClass("com.mysql.jdbc.Driver");
//        ds.setJdbcUrl("jdbc:mysql:/hadoop01:3306/mybatis_test");
//        ds.setUser("root");
//        ds.setPassword("123456");
//        return ds;
//    }
}
