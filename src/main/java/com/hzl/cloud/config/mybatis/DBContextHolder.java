package com.hzl.cloud.config.mybatis;

import com.hzl.cloud.constant.DBTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * description
 * 设置，查询，清楚当前线程的数据源信息
 * @author hzl 2020/01/08 10:54 AM
 */
@Slf4j
public class DBContextHolder {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

	public static void setDataSource(String dataSource) {
		log.info("注解设置的数据源"+dataSource);
		contextHolder.set(dataSource);
	}

	public static String getDataSource() {
		return contextHolder.get();
	}

	public static void clearDataSource() {
		contextHolder.remove();
	}

}
