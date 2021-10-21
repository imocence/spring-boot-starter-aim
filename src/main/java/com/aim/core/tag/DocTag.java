package com.aim.core.tag;


/**
 * @AUTO 针对注释标签
 * @Author AIM
 * @DATE 2021/10/21
 */
public abstract class DocTag<T> {

    /**
     * 标签名称
     */
    private String tagName;

    public DocTag(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName(){
        return tagName;
    }

    public abstract T getValues();

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
