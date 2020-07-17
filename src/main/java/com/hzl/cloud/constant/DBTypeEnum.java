package com.hzl.cloud.constant;

/**
 * description
 *
 * @author hzl 2020/01/07 11:36 PM
 */
public enum DBTypeEnum {

	MASTER("master"),
	SLAVE1("salve1"),
	SLAVE2("slave2");
	private final String value;

	DBTypeEnum(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
