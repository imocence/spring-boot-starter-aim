package com.aim.spring.framework;

import java.util.List;

/**
 * @Author AIM
 */
public class ParamInfo {

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

    /**
     *
     */
    private List<ParamInfo> properties;

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

    public List<ParamInfo> getProperties() {
        return properties;
    }

    public void setProperties(List<ParamInfo> properties) {
        this.properties = properties;
    }
}
