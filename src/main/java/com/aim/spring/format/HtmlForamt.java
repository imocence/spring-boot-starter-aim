package com.aim.spring.format;

import com.aim.core.format.Format;
import com.aim.core.model.ApiDoc;
import com.aim.core.utils.JsonUtils;
import com.aim.core.utils.AimUtil;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author AIM
 */
public class HtmlForamt implements Format {

	@Override
	public String format(ApiDoc apiDoc) {
		InputStream in = HtmlForamt.class.getResourceAsStream("html.vm");
		if (in != null) {
			try {
				String s = IOUtils.toString(in, "utf-8");
				Map<String, Object> properties = new HashMap<>();
				properties.put("title", AimUtil.defaultString((String) apiDoc.getProperties().get("title"), "接口文档"));
				properties.put("version", AimUtil.defaultString((String) apiDoc.getProperties().get("version"), "1.0"));
				Map<String, Object> model = new HashMap<>();
				model.put("properties", properties);
				model.put("apiModules", apiDoc.getApiModules());

				return s.replace("_apis_json", JsonUtils.toJson(model));
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(in);
			}
		}
		return "";
	}
}
