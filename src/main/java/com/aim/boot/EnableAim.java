package com.aim.boot;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @AUTO 插件开关
 * @Author AIM
 * @DATE 2021/10/21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(AimConfiguration.class)
@EnableConfigurationProperties(AimProperties.class)
public @interface EnableAim {
}
