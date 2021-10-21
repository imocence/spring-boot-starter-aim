package com.aim.core.tag;

import com.aim.core.utils.Constant;
import com.aim.core.utils.AimUtil;

/**
 * @AUTO 针对@Resp注释的内容封装实现
 * @Author AIM
 * @DATE 2021/10/21
 */
public class RespTagImpl extends DocTag<String> {

    /**
     * 参数名
     */
    private String paramName;

    /**
     * 参数描述
     */
    private String paramDesc;

    /**
     * 是否必填,默认false
     */
    private boolean require;

    /**
     * 类型
     */
    private String paramType;

    public RespTagImpl(String tagName, String paramName, String paramDesc, String paramType, boolean require) {
        super(tagName);
        this.paramName = paramName;
        this.paramDesc = paramDesc;
        this.paramType = paramType;
        this.require = require;
    }

    @Override
    public String getValues() {
        String s = paramName + " " + paramDesc;
        if (AimUtil.isNotBlank(paramType)) {
            s += " " + paramType;
        }
        s += " " + (require ? Constant.YES_ZH : Constant.NOT_ZH);
        return s;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public boolean isRequire() {
        return require;
    }

    public void setRequire(boolean require) {
        this.require = require;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }
}
