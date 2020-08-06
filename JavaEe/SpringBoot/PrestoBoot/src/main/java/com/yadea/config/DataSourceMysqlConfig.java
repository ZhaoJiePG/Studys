package com.yadea.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by ZJ on 2020/7/30
 * comment:
 */

@Configuration
@MapperScan(basePackages = "com.yadea.mapper.mysql", sqlSessionTemplateRef = "paSqlSessionTemplate")
@Slf4j
public class DataSourceMysqlConfig {
    @Bean(name = "paDataSource")
    @ConfigurationProperties(prefix = "mysql.spring.datasource.pa")
    @Primary
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "paSqlSessionFactory")
    @Primary
    public MybatisSqlSessionFactoryBean testSqlSessionFactory(@Qualifier("paDataSource") DataSource dataSource) throws Exception {
        log.info("加载mysql数据库连接......");
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/mysql/*Mapper.xml"));
        MybatisConfiguration configuration = new MybatisConfiguration();
//		configuration.setLogImpl(StdOutImpl.class);
        configuration.setMapUnderscoreToCamelCase(true);
        factory.setConfiguration(configuration);
        factory.setTypeAliasesPackage("com.central.data.model.mysql");
        factory.setPlugins(new Interceptor[] {paginationInterceptor()});
        return factory;
    }

    @Bean(name = "paTransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("paDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "paSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("paSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
