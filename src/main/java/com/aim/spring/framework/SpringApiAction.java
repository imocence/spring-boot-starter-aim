package com.aim.spring.framework;


import com.aim.core.model.ApiAction;

import java.util.List;

/**
 * @Author AIM
 */
public class SpringApiAction extends ApiAction {

    /**
     * 访问的uri地址
     */
    private List<String> uris;

    /**
     * 允许的访问方法:POST,GET,DELETE,PUT等, 如果没有,则无限制
     */
    private List<String> methods;

    /**
     * 入参
     */
    private List<ParamInfo> param;

    /**
     * 出参
     */
    private List<ParamInfo> returnParam;

    /**
     * 返回描述
     */
    private String returnDesc;

    /**
     * 返回的数据
     */
    private String respbody;

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

    public List<ParamInfo> getParam() {
        return param;
    }

    public void setParam(List<ParamInfo> param) {
        this.param = param;
    }

    public List<ParamInfo> getReturnParam() {
        return returnParam;
    }

    public void setReturnParam(List<ParamInfo> returnParam) {
        this.returnParam = returnParam;
    }

    public String getReturnDesc() {
        return returnDesc;
    }

    public void setReturnDesc(String returnDesc) {
        this.returnDesc = returnDesc;
    }

    public String getRespbody() {
        return respbody;
    }

    public void setRespbody(String respbody) {
        this.respbody = respbody;
    }

    public boolean isJson() {
        return json;
    }

    public void setJson(boolean json) {
        this.json = json;
    }
}
