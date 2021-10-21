package com.aim.core.resolver;

import com.aim.core.model.ApiModule;

import java.util.List;

/**
 * @AUTO 所有的解析器实现都要继承此接口，默认使用javaparser实现
 * @Author AIM
 * @DATE 2021/10/21
 */
public interface Resolver {

    /**
     * 解析文件方法
     * @param files
     */
    List<ApiModule> resolve(List<String> files);
}
