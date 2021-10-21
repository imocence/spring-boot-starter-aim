package com.aim.core.framework;

import com.aim.core.model.ApiModule;

import java.util.List;

/**
 * @AUTO 抽象各种API框架的特性,用于在基于aim-core渲染出来的ApiModule基础中,进行再度包装
 * @Author AIM
 * @DATE 2021/10/21
 */
public interface Framework {

	/**
	 * 扩展API数据
	 *
	 * @param apiModules 原始基本的Api数据
	 * @return 扩展后的api数据
	 */
	List<ApiModule> extend(List<ApiModule> apiModules, String reqUrl, String defResult);

	/**
	 * 判断该类是否适合该框架
	 *
	 * @param classz 扫描到的类
	 * @return 是支持
	 */
	boolean support(Class<?> classz);
}
