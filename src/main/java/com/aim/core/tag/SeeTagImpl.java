package com.aim.core.tag;

import com.aim.core.model.ObjectInfo;

/**
 * @AUTO 针对@see注释标签进行封装实现
 * @Author AIM
 * @DATE 2021/10/21
 */
public class SeeTagImpl extends DocTag<ObjectInfo> {

    private ObjectInfo objectInfo;

    public SeeTagImpl(String tagName, ObjectInfo objectInfo) {
        super(tagName);
        this.objectInfo = objectInfo;
    }

    @Override
    public ObjectInfo getValues() {
        return objectInfo;
    }
}
