package com.aim.core.tag;

/**
 * @AUTO 简单文本型注释标签实现
 * @Author AIM
 * @DATE 2021/10/21
 */
public class DocTagImpl extends DocTag<String> {

    public String value;

    public DocTagImpl(String tagName, String value) {
        super(tagName);
        this.value = value;
    }

    @Override
    public String getValues() {
        return value;
    }
}
