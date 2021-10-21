package com.aim.core.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @AUTO API的参数说明
 * @Author AIM
 * @DATE 2021/10/21
 */
public class ApiDoc {

	/**
	 * 附带的属性
	 */
	private Map<String, Object> properties = new HashMap<>();

	/**
	 * 所有API模块
	 */
	private List<ApiModule> apiModules;

	public ApiDoc(List<ApiModule> apiModules) {
		this.apiModules = apiModules;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public List<ApiModule> getApiModules() {
		return apiModules;
	}

	public void setApiModules(List<ApiModule> apiModules) {
		this.apiModules = apiModules;
	}
}
