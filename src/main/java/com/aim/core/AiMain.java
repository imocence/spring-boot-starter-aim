package com.aim.core;

import com.aim.core.format.Format;
import com.aim.core.framework.Framework;
import com.aim.core.model.ApiDoc;
import com.aim.core.model.ApiModule;
import com.aim.core.resolver.Resolver;
import com.aim.core.resolver.javaparser.JavaParserDocTagResolver;
import com.aim.core.utils.AimUtil;
import com.aim.core.utils.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @AUTO 入口, 核心处理从这里开始
 * @Author AIM
 * @DATE 2021/10/21
 */
public class AiMain {

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 源码路径
	 */
	private List<String> srcPaths;
	/**
	 * 请求接口路径
	 */
	private String reqUrl;
	/**
	 * 指定返回结果样式
	 */
	private String defResult;
	/**
	 * api框架类型
	 */
	private List<Framework> frameworks = new ArrayList<>();

	/**
	 * 默认的java注释解析器实现
	 * <p>
	 * 备注:基于sun doc的解析方式已经废弃,若需要请参考v1.0之前的版本
	 *
	 * @see JavaParserDocTagResolver
	 */
	private Resolver docTagResolver = new JavaParserDocTagResolver();

	/**
	 * 构建aim对象
	 *
	 * @param srcPath 源码路径
	 */
	public AiMain(String srcPath, Framework framework) {
		this(Collections.singletonList(srcPath), framework);
	}

	/**
	 * 构建aim对象
	 *
	 * @param srcPaths 源码路径,支持多个
	 */
	public AiMain(List<String> srcPaths, Framework framework) {
		this.srcPaths = srcPaths;
		frameworks.add(framework);
	}

	/**
	 * 解析源码并返回对应的接口数据
	 *
	 * @return API接口数据
	 */
	public ApiDoc resolve() {
		// java doc 基础解析
		List<ApiModule> apiModules = this.docTagResolver.resolve(getAllFiles());

		// 附加解析
		for (Framework framework : frameworks) {
			apiModules = framework.extend(apiModules, getReqUrl(), getDefResult());
		}

		return new ApiDoc(apiModules);
	}

	private List<String> getAllFiles() {
		List<String> files = new ArrayList<>();
		for (String srcPath : this.srcPaths) {
			File dir = new File(srcPath);
			log.info("解析源码路径:{}", dir.getAbsolutePath());
			files.addAll(FileUtils.getAllJavaFiles(dir));
		}
		return files;
	}

	/**
	 * 构建接口文档
	 *
	 * @param out    输出位置
	 * @param format 文档格式
	 */
	public void build(OutputStream out, Format format) {
		this.build(out, format, null);
	}

	/**
	 * 构建接口文档
	 *
	 * @param out        输出位置
	 * @param format     文档格式
	 * @param properties 文档属性
	 */
	public void build(OutputStream out, Format format, Map<String, Object> properties) {
		ApiDoc apiDoc = this.resolve();
		if (properties != null) {
			apiDoc.getProperties().putAll(properties);
		}

		if (apiDoc.getApiModules() != null && out != null && format != null) {
			String s = format.format(apiDoc);
			try {
				IOUtils.write(s, out, AimUtil.CHARSET);
			} catch (IOException e) {
				log.error("接口文档写入文件失败", e);
			} finally {
				IOUtils.closeQuietly(out);
			}
		}
	}

	/** GET & SET **/

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

	public void setFrameworks(List<Framework> frameworks) {
		this.frameworks = frameworks;
	}

	public void setDocTagResolver(Resolver docTagResolver) {
		this.docTagResolver = docTagResolver;
	}
}
