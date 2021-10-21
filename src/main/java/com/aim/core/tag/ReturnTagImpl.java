package com.aim.core.tag;

/**
 * @AUTO 针对@Return注释的内容封装实现
 * @Author AIM
 * @DATE 2021/10/21
 */
public class ReturnTagImpl extends DocTag<String>{

    private String value;

    public ReturnTagImpl(String tagName) {
        super(tagName);
    }

    public ReturnTagImpl(String tagName, String value) {
        super(tagName);
        this.value = value;
    }

    @Override
    public String getValues() {
        return value;
    }
}