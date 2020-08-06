package com.yadea.config;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
/**
 * Created by ZJ on 2020/7/30
 * comment:
 */
@Configuration
@MapperScan(basePackages = "com.yadea.mapper.presto", sqlSessionTemplateRef = "marketingSqlSessionTemplate")
@Slf4j
public class DataSourcePrestoConfig {

    @Bean(name = "marketingDataSource")
    @ConfigurationProperties(prefix = "presto.spring.datasource.marketing")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "marketingSqlSessionFactory")
    public MybatisSqlSessionFactoryBean testSqlSessionFactory(@Qualifier("marketingDataSource") DataSource dataSource) throws Exception {
        log.info("加载presto数据库连接......");
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/presto/*Mapper.xml"));
        MybatisConfiguration configuration = new MybatisConfiguration();
//		configuration.setLogImpl(StdOutImpl.class);
        configuration.setMapUnderscoreToCamelCase(true);
        factory.setConfiguration(configuration);
        factory.setTypeAliasesPackage("com.central.data.model.presto");
//        factory.setPlugins(new Interceptor[] {paginationInterceptor()});
        return factory;
    }

    @Bean(name = "marketingTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("marketingDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "marketingSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("marketingSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
