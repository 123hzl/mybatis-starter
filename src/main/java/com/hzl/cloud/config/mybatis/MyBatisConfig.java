package com.hzl.cloud.config.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * description
 * 给数据源设置事务
 *
 * @author hzl 2020/01/07 11:14 PM
 */
@EnableTransactionManagement
@Configuration
public class MyBatisConfig {

	@Resource(name = "dataSource")
	private DataSource myRoutingDataSource;

	@Value("${mybatis.mapperLocations}")
	private String scanMapperPath;

	@Bean(name = "clusterSqlSessionFactory")
	@Primary
	public SqlSessionFactory sqlSessionFactory() throws Exception {

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(myRoutingDataSource);
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(scanMapperPath));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager platformTransactionManager() {
		return new DataSourceTransactionManager(myRoutingDataSource);
	}

//	@Bean
//	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//		return new SqlSessionTemplate(sqlSessionFactory);
//	}
}
