package com.aim.core.resolver.javaparser.converter;

import com.aim.core.tag.DocTag;
import com.aim.core.tag.ParamTagImpl;
import com.aim.core.tag.RespTagImpl;

/**
 * @AUTO 针对@resp的转换器
 * @Author AIM
 * @DATE 2021/10/21
 */
public class RespTagConverter extends ParamTagConverter {

    @Override
    public DocTag converter(String comment) {
        ParamTagImpl paramTag = (ParamTagImpl) super.converter(comment);
        RespTagImpl respTag = new RespTagImpl(paramTag.getTagName(), paramTag.getParamName(), paramTag.getParamDesc(),
                paramTag.getParamType(), paramTag.isRequire());
        return respTag;
    }
}
