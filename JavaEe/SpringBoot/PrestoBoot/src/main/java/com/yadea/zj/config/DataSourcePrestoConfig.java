package com.yadea.zj.config;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@MapperScan(basePackages = "com.yadea.zj.mapper", sqlSessionTemplateRef = "prestoJdbcSqlSessionTemplate")
public class DataSourcePrestoConfig {

	private static final Logger log = LoggerFactory.getLogger(DataSourcePrestoConfig.class);

	@Bean(name = "prestoJdbcDataSource")
	@ConfigurationProperties(prefix = "presto.datasource")
	public DataSource testDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "prestoJdbcSqlSessionFactory")
	public SqlSessionFactory testSqlSessionFactory(@Qualifier("prestoJdbcDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/presto/DmDimDayMapper.xml"));
		return bean.getObject();
	}

	@Bean(name = "prestoJdbcTransactionManager")
	public DataSourceTransactionManager testTransactionManager(@Qualifier("prestoJdbcDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "prestoJdbcSqlSessionTemplate")
	public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("prestoJdbcSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
