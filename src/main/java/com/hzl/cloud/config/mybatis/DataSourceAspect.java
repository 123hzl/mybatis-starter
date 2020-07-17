package com.hzl.cloud.config.mybatis;

import com.hzl.cloud.constant.DBTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * description
 * 通过切片读取注解@DataSource，在建立初始化连接前指定当前线程的数据源
 *
 * @author hzl 2020/01/07 9:59 PM
 */
@Slf4j
@Aspect
@Component
public class  DataSourceAspect {

	@Value("${hadoop.openMulti: false}")
	private Boolean openMulti;

	@Pointcut("@annotation(com.hzl.cloud.config.mybatis.DataSource)")
	public void dataSourcePointCut() {

	}

	@Around("dataSourcePointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();

		//获取参数,不能获取对象集成的父类
		Object[] args = point.getArgs();
		//获取参数值,不能获取对象集成的父类
		ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
		String[] parameterNames = pnd.getParameterNames(method);
		//封装参数map
		Map<String, Object> paramMap = new HashMap<>(32);
		for (int i = 0; i < parameterNames.length; i++) {
			paramMap.put(parameterNames[i], args[i]);
		}
		log.info("方法参数"+paramMap.toString());

		//获取方法返回值
		Object returnResult = point.proceed(args);
		log.info("打印返回结果" + returnResult.toString());

		DataSource dataSource = method.getAnnotation(DataSource.class);
		if (dataSource == null) {
			DBContextHolder.setDataSource(DBTypeEnum.MASTER.value());
		} else if (DBTypeEnum.MASTER.value().equals(dataSource.name())) {
			DBContextHolder.setDataSource(dataSource.name());
		} else if (!openMulti) {
			//如果不启动多数据，则默认主数据源连接
			DBContextHolder.setDataSource(DBTypeEnum.MASTER.value());
		} else {
			DBContextHolder.setDataSource(dataSource.name());
		}

		try {
			return point.proceed();
		} finally {
			DBContextHolder.clearDataSource();
		}
	}

}
