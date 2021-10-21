package com.aim.core.resolver.javaparser.converter;

import com.aim.core.tag.DocTag;

/**
 * @AUTO 针对JavaParser语法解析包解析出来的文本转换器,负责将文本转转DocTag
 * @Author AIM
 * @DATE 2021/10/21
 */
public interface JavaParserTagConverter<T extends String> {

    /**
     * 将指定的文本转义为DocTag
     *
     * @param o 文本
     * @return DocTag对象
     */
    DocTag converter(T o);
}
