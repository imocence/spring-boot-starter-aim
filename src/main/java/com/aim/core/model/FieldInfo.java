package com.aim.core.model;

import java.util.List;

/**
 * @AUTO 反射信息
 * @Author AIM
 * @DATE 2021/10/21
 */
public class FieldInfo {

	private String name;

	private Class<?> type;

	private String simpleTypeName;

	private String comment;

	/**
	 * 是否必填,默认false
	 */
	private boolean require;

	private List<FieldInfo> fieldInfos;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getSimpleTypeName() {
		return simpleTypeName;
	}

	public void setSimpleTypeName(String simpleTypeName) {
		this.simpleTypeName = simpleTypeName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isRequire() {
		return require;
	}

	public void setRequire(boolean require) {
		this.require = require;
	}

	public List<FieldInfo> getFieldInfos() {
		return fieldInfos;
	}

	public void setFieldInfos(List<FieldInfo> fieldInfos) {
		this.fieldInfos = fieldInfos;
	}
}
