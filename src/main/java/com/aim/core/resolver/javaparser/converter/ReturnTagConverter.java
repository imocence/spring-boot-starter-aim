package com.aim.core.resolver.javaparser.converter;

import com.aim.core.tag.DocTag;
import com.aim.core.tag.DocTagImpl;
import com.aim.core.tag.ReturnTagImpl;

/**
 * @AUTO 针对@Return的转换器
 * @Author AIM
 * @DATE 2021/10/21
 */
public class ReturnTagConverter extends DefaultJavaParserTagConverterImpl {

    @Override
    public DocTag converter(String comment) {
        DocTagImpl docTag = (DocTagImpl) super.converter(comment);
        return new ReturnTagImpl(docTag.getTagName(), docTag.value);
    }
}
