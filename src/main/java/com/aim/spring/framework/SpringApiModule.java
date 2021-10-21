package com.aim.spring.framework;

import com.aim.core.model.ApiModule;

import java.util.List;

/**
 * @Author AIM
 */
public class SpringApiModule extends ApiModule {

    /**
     * 业务模块首地址
     */
    private List<String> uris;

    /**
     * 接口限制必须采用访问方式
     */
    private List<String> methods;

    /**
     * 是否返回json
     */
    private boolean json;

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public boolean isJson() {
        return json;
    }

    public void setJson(boolean json) {
        this.json = json;
    }
}
