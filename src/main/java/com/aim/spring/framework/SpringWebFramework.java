package com.aim.spring.framework;

import com.aim.core.framework.Framework;
import com.aim.core.model.ApiAction;
import com.aim.core.model.ApiModule;
import com.aim.core.model.FieldInfo;
import com.aim.core.model.ObjectInfo;
import com.aim.core.resolver.javaparser.converter.SeeTagConverter;
import com.aim.core.tag.DocTag;
import com.aim.core.tag.ParamTagImpl;
import com.aim.core.tag.RespTagImpl;
import com.aim.core.tag.SeeTagImpl;
import com.aim.core.utils.ClassMapperUtils;
import com.aim.core.utils.CommentUtils;
import com.aim.core.utils.TagUtils;
import com.aim.core.utils.HttpUtils;
import com.aim.core.utils.AimUtil;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基于spirng web框架,扩展api接口数据
 * <p>
 *
 * @author AIM
 * @date 2018/6/22
 */
public class SpringWebFramework implements Framework {

	private Logger log = LoggerFactory.getLogger(SpringWebFramework.class);

	@Override
	public boolean support(Class<?> classz) {
		return classz.getAnnotation(Controller.class) != null || classz.getAnnotation(RestController.class) != null;
	}

	@Override
	public List<ApiModule> extend(List<ApiModule> apiModules, String reqUrl, String defResult) {
		if (apiModules == null) {
			return new ArrayList<>(0);
		}
		List<ApiModule> list = new ArrayList<>(apiModules.size());
		for (ApiModule apiModule : apiModules) {
			SpringApiModule sam = new SpringApiModule();

			sam.setComment(apiModule.getComment());
			sam.setType(apiModule.getType());
			boolean isjson = this.isJson(apiModule.getType());
			sam.setJson(isjson);

			RequestMapping classRequestMappingAnno = apiModule.getType().getAnnotation(RequestMapping.class);

			if (classRequestMappingAnno != null) {
				sam.setUris(this.getUris(classRequestMappingAnno.value()));
				sam.setMethods(this.getMethods(classRequestMappingAnno.method()));
			} else {
				sam.setUris(new ArrayList<>(0));
				sam.setMethods(new ArrayList<>(0));
			}
			String url = "";
			if (sam.getUris().size() > 0) {
				url = reqUrl + sam.getUris().get(0);
			} else {
				url = reqUrl;
			}
			for (ApiAction apiAction : apiModule.getApiActions()) {
				SpringApiAction saa = this.buildSpringApiAction(apiAction, isjson);
				if (saa != null) {
					String name = saa.getName();
					if (name.contains("add") || name.contains("creat") || name.contains("del") || name.contains("remove")) {
						saa.setRespbody(defResult);
					} else {
						try {
							if (saa.getMethods().contains("POST")) {
								saa.setRespbody(HttpUtils.getInstance().postForm(url + saa.getUris().get(0)));
							} else {
								saa.setRespbody(HttpUtils.getInstance().getForm(url + saa.getUris().get(0)));
							}
						} catch (IOException e) {
							log.info("获取请求结果失败：{}", e.getMessage());
						}
					}
					sam.getApiActions().add(saa);
				}
			}

			list.add(sam);
		}
		return list;
	}

	/**
	 * 构建基于spring web的接口
	 *
	 * @param apiAction 请求的Action信息
	 * @param isjson    是否json
	 * @return 封装后的机遇SpringWeb的Action信息
	 */
	private SpringApiAction buildSpringApiAction(ApiAction apiAction, boolean isjson) {
		SpringApiAction saa = new SpringApiAction();
		saa.setName(apiAction.getName());
		saa.setComment(apiAction.getComment());
		saa.setMethod(apiAction.getMethod());
		saa.setDocTags(apiAction.getDocTags());

		if (isjson || apiAction.getMethod().getAnnotation(ResponseBody.class) != null) {
			saa.setJson(true);
		}

		saa.setTitle(getTitile(saa));
		saa.setRespbody(getRespbody(saa));

		boolean isMappingMethod = this.setUrisAndMethods(apiAction, saa);

		if (!isMappingMethod) {
			return null;
		}

		SeeTagConverter seeTagConverter = new SeeTagConverter();
		Method method = apiAction.getMethod();
		Class returnClassz = method.getReturnType();

		String path = ClassMapperUtils.getPath(returnClassz.getSimpleName());
		if (path != null) {
			FileInputStream in = null;
			try {
				in = new FileInputStream(path);
			} catch (FileNotFoundException e) {
			}
			CompilationUnit cu = JavaParser.parse(in);
			String text = cu.getComment().isPresent() ? CommentUtils.parseCommentText(cu.getComment().get().getContent()) : "";
			Map<String, String> respMap = seeTagConverter.analysisFieldComments(returnClassz);
			List<FieldInfo> fields = seeTagConverter.analysisFields(returnClassz, respMap);
			ObjectInfo objectInfo = new ObjectInfo();
			objectInfo.setType(returnClassz);
			objectInfo.setFieldInfos(fields);
			objectInfo.setComment(text);
//            saa.setReturnObj(objectInfo);
		}
		// 参数集合
		List<ParamInfo> paramInfos = new ArrayList<>();
		MethodDeclaration methodDeclaration = apiAction.getMethodDeclaration();
		NodeList<Parameter> nodeList = methodDeclaration.getParameters();
		for (Parameter parameter : nodeList) {
			ParamInfo paramInfo = new ParamInfo();
			paramInfo.setParamType(parameter.getType().toString());
			paramInfo.setParamName(parameter.getName().toString());
			if (AimUtil.isNotEmpty(parameter.getAnnotations())) {
				for (AnnotationExpr annotationExpr : parameter.getAnnotations()) {
					if (annotationExpr.getNameAsString().equals("NotNull")) {
						paramInfo.setRequire(true);
					}
					if (annotationExpr.getNameAsString().equals("RequestParam")) {
						if (annotationExpr.isSingleMemberAnnotationExpr()) {
							paramInfo.setParamName(annotationExpr.asSingleMemberAnnotationExpr().getNameAsString());
						}
						paramInfo.setRequire(true);
						List<Node> childNodes = annotationExpr.getChildNodes();
						for (Node node : childNodes) {
							if (node instanceof MemberValuePair) {
								if ("required".equals(((MemberValuePair) node).getNameAsString())) {
									paramInfo.setRequire("true".equals(((MemberValuePair) node).getValue().toString()));
								}
							}
						}
					}
				}
			}
			paramInfos.add(paramInfo);
		}

		// 返回值
		saa.setParam(paramInfos);
		this.hatchParams(saa);
//      saa.setRespParam(this.getResp(saa));
//      saa.setReturnObj(this.getSeeObj(saa));
//      saa.setReturnObj(objectInfo);
		saa.setReturnDesc(this.getReturnDesc(saa));

		return saa;
	}

	/**
	 * 设置请求地址和请求方法
	 */
	private boolean setUrisAndMethods(ApiAction apiAction, SpringApiAction saa) {
		RequestMapping methodRequestMappingAnno = apiAction.getMethod().getAnnotation(RequestMapping.class);
		if (methodRequestMappingAnno != null) {
			saa.setUris(this.getUris(methodRequestMappingAnno.value()));
			saa.setMethods(this.getMethods(methodRequestMappingAnno.method()));
			return true;
		}

		PostMapping postMapping = apiAction.getMethod().getAnnotation(PostMapping.class);
		if (postMapping != null) {
			saa.setUris(this.getUris(postMapping.value()));
			saa.setMethods(this.getMethods(RequestMethod.POST));
			return true;
		}

		GetMapping getMapping = apiAction.getMethod().getAnnotation(GetMapping.class);
		if (getMapping != null) {
			saa.setUris(this.getUris(getMapping.value()));
			saa.setMethods(this.getMethods(RequestMethod.GET));
			return true;
		}

		PutMapping putMapping = apiAction.getMethod().getAnnotation(PutMapping.class);
		if (putMapping != null) {
			saa.setUris(this.getUris(putMapping.value()));
			saa.setMethods(this.getMethods(RequestMethod.PUT));
			return true;
		}

		DeleteMapping deleteMapping = apiAction.getMethod().getAnnotation(DeleteMapping.class);
		if (deleteMapping != null) {
			saa.setUris(this.getUris(deleteMapping.value()));
			saa.setMethods(this.getMethods(RequestMethod.DELETE));
			return true;
		}

		PatchMapping patchMapping = apiAction.getMethod().getAnnotation(PatchMapping.class);
		if (patchMapping != null) {
			saa.setUris(this.getUris(patchMapping.value()));
			saa.setMethods(this.getMethods(RequestMethod.PATCH));
			return true;
		}
		return false;
	}

	/**
	 * 获取@return注释上的描述语
	 */
	protected String getReturnDesc(SpringApiAction saa) {
		DocTag tag = TagUtils.findTag(saa.getDocTags(), "@return");
		return tag != null ? tag.getValues().toString() : null;
	}

	/**
	 * 获取@ss注释上的对象
	 */
	protected ObjectInfo getSeeObj(SpringApiAction saa) {
		SeeTagImpl tag = (SeeTagImpl) TagUtils.findTag(saa.getDocTags(), "@see");
		return tag != null ? tag.getValues() : null;
	}

	/**
	 * 获取@param注释上的信息
	 */
	private void hatchParams(SpringApiAction saa) {
		List tags = TagUtils.findTags(saa.getDocTags(), "@param");
		for (Object tag : tags) {
			ParamTagImpl paramTag = (ParamTagImpl) tag;
			for (ParamInfo paramInfo : saa.getParam()) {
				if (paramTag.getParamName().equals(paramInfo.getParamName())) {
					paramInfo.setParamDesc(paramTag.getParamDesc());
				}
			}
		}
	}

	/**
	 * 获取@resp注释上的信息
	 */
	protected List<ParamInfo> getResp(SpringApiAction saa) {
		List<DocTag> tags = TagUtils.findTags(saa.getDocTags(), "@resp");
		List<ParamInfo> list = new ArrayList(tags.size());
		for (DocTag tag : tags) {
			RespTagImpl respTag = (RespTagImpl) tag;
			ParamInfo paramInfo = new ParamInfo();
			paramInfo.setParamName(respTag.getParamName());
			paramInfo.setRequire(respTag.isRequire());
			paramInfo.setParamDesc(respTag.getParamDesc());
			paramInfo.setParamType(respTag.getParamType());
			list.add(paramInfo);
		}
		return list;
	}

	/**
	 * 获取@respbody上的信息
	 */
	protected String getRespbody(SpringApiAction saa) {
		DocTag respbodyTag = TagUtils.findTag(saa.getDocTags(), "@respbody");
		if (respbodyTag != null) {
			return (String) respbodyTag.getValues();
		}
		return null;
	}

	/**
	 * 获取@title上的信息
	 */
	protected String getTitile(SpringApiAction saa) {
		DocTag titleTag = TagUtils.findTag(saa.getDocTags(), "@title");
		if (titleTag != null) {
			return (String) titleTag.getValues();
		} else {
			return saa.getComment();
		}
	}

	/**
	 * 获取接口的uri
	 */
	protected List<String> getUris(String[] values) {
		List<String> uris = new ArrayList<>();
		for (String value : values) {
			uris.add(value);
		}
		return uris;
	}

	/**
	 * 获取接口上允许的访问方式
	 */
	protected List<String> getMethods(RequestMethod... methods) {
		List<String> methodStrs = new ArrayList<>();
		for (RequestMethod requestMethod : methods) {
			methodStrs.add(requestMethod.name());
		}
		return methodStrs;
	}

	/**
	 * 判断整个类里的所有接口是否都返回json
	 */
	protected boolean isJson(Class<?> classz) {
		RestController restControllerAnno = classz.getAnnotation(RestController.class);
		ResponseBody responseBody = classz.getAnnotation(ResponseBody.class);
		return responseBody != null || restControllerAnno != null;
	}
}
