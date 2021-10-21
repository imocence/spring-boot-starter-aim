package com.aim.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedList;
import java.util.List;

/**
 * @AUTO 接口业务模块,一个接口类为一个模块
 * @Author AIM
 * @DATE 2021/10/21
 */
public class ApiModule {

    /**
     * 源码在哪个类
     */
    @JsonIgnore
    private transient Class<?> type;

    /**
     * 业务模块的描述
     */
    private String comment;

    /**
     * 此业务模块下有哪些接口
     */
    private List<ApiAction> apiActions = new LinkedList<>();

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ApiAction> getApiActions() {
        return apiActions;
    }

    public void setApiActions(List<ApiAction> apiActions) {
        this.apiActions = apiActions;
    }
}
