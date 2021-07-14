package com.hzl.cloud.config.mybatis;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.hzl.cloud.constant.DBTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * description
 * 多数据
 * @author hzl 2020/01/07 9:54 PM
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "hadoop", name = "openMulti", havingValue = "true", matchIfMissing = false)
@AutoConfigureBefore({DruidDataSourceAutoConfigure.class,DataSourceAutoConfiguration.class})
public class MultiDataSourceConfig {

	@Bean(name="masterDataSource")
	@ConfigurationProperties("spring.datasource.master")
	public DataSource masterDataSource() {
		log.info("注入主数据源");
		return DruidDataSourceBuilder.create().build();
	}

	@Bean(name="slaveDataSource")
	@ConfigurationProperties("spring.datasource.slave1")
	public DataSource slaveDataSource() {
		log.info("注入slave1数据源");
		return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = "dataSource")
	@Primary
	public DynamicDataSource dataSource(@Qualifier("masterDataSource") DataSource masterDataSource, @Qualifier("slaveDataSource") DataSource slaveDataSource) {
		log.info("启动多数据源");
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DBTypeEnum.MASTER.value(), masterDataSource);
		if (slaveDataSource != null) {
			targetDataSources.put(DBTypeEnum.SLAVE1.value(), slaveDataSource);
		}
		return new DynamicDataSource(masterDataSource, targetDataSources);
	}

}
