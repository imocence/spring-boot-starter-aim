package com.aim.core.resolver.javaparser.converter;

import com.aim.core.tag.DocTag;
import com.aim.core.tag.DocTagImpl;
import com.aim.core.utils.CommentUtils;

/**
 * @AUTO 基于JavaParser包的默认注释解析转换器
 * @Author AIM
 * @DATE 2021/10/21
 */
public class DefaultJavaParserTagConverterImpl implements JavaParserTagConverter<String> {

    @Override
    public DocTag converter(String comment) {
        String tagType = CommentUtils.getTagType(comment);
        String coment = comment.substring(tagType.length()).trim();
        return new DocTagImpl(tagType, coment);
    }
}
