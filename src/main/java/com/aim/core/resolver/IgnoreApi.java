package com.aim.core.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @AUTO 忽略生成接口文档,标注了该注解的接口,将不会生成接口文档
 * @Author AIM
 * @DATE 2021/10/21
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreApi {
}
