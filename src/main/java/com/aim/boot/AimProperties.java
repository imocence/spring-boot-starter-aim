package com.aim.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @AUTO 自动获取配置文件参数
 * @Author AIM
 * @DATE 2021/10/21
 */
@ConfigurationProperties("aim")
public class AimProperties {

    /**
     * 是否启动aim,此值便于在生产等环境启动程序时增加参数进行控制
     */
    private boolean enable = true;

    /**
     * 界面标题描述
     */
    private String title = "aim 接口文档";

    /**
     * 源码相对路径(支持多个,用英文逗号隔开)
     */
    private String sourcePath;

    /**
     * 文档版本号
     */
    private String version;
    /**
     * 请求接口的基础地址
     */
    private String reqUrl;
    /**
     * 默认返回信息结构
     */
    private String defResult;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getDefResult() {
        return defResult;
    }

    public void setDefResult(String defResult) {
        this.defResult = defResult;
    }
}
