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
 *	单数据配置
 * @author hzl 2020/01/17 1:39 PM
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "hadoop", name = "openMulti", havingValue = "false", matchIfMissing = true)
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
public class MasterDataSourceConfig {

	@Bean
	@ConfigurationProperties("spring.datasource.master")
	@Primary
	public DataSource masterDataSource() {
		log.info("注入主数据源");
		return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = "dataSource")
	public DynamicDataSource dataSource(@Qualifier("masterDataSource") DataSource masterDataSource) {
		log.info("启动单数据源");
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DBTypeEnum.MASTER.value(), masterDataSource);
		return new DynamicDataSource(masterDataSource, targetDataSources);
	}

}
