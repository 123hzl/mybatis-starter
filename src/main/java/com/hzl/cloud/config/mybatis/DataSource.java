package com.hzl.cloud.config.mybatis;

/**
 * description
 *
 * @author hzl 2020/01/07 9:58 PM
 */

import java.lang.annotation.*;

/**
 * 备注：自定义数据源选择注解
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
	String name() default "master";
}
