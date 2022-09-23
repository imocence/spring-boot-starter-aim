package com.aim.spring.format;

import com.aim.core.format.Format;
import com.aim.core.model.ApiAction;
import com.aim.core.model.ApiDoc;
import com.aim.core.model.ApiModule;
import com.aim.core.utils.JsonFormatUtils;
import com.aim.spring.framework.SpringApiAction;
import com.aim.core.utils.AimUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author AIM
 */
public class MarkdownFormat implements Format {

    private Logger log = LoggerFactory.getLogger(getClass());
    private VelocityTemplater templater = new VelocityTemplater("com/aim/spring/format/api.vm");

    @Override
    public String format(ApiDoc apiDoc) {
        StringBuilder sb = new StringBuilder();
        for (ApiModule apiModule : apiDoc.getApiModules()) {
            sb.append(format(apiModule)).append("\n\n");
        }
        return sb.toString();
    }

    private String format(ApiModule apiModule) {
        for (ApiAction apiAction : apiModule.getApiActions()) {
            SpringApiAction saa = (SpringApiAction) apiAction;
            if (saa.isJson() && AimUtil.isNotBlank(saa.getRespbody())) {
                saa.setRespbody(JsonFormatUtils.formatJson(saa.getRespbody()));
            }
        }

        try {
            Map<String, Object> map = PropertyUtils.describe(apiModule);
            return templater.parse(map);
        } catch (Exception e) {
            log.error("输出markdown文档格式失败", e);
        }
        return null;
    }
}
