package com.aim.core.resolver.javaparser;

import com.aim.core.model.ApiAction;
import com.aim.core.model.ApiModule;
import com.aim.core.resolver.IgnoreApi;
import com.aim.core.resolver.Resolver;
import com.aim.core.resolver.javaparser.converter.JavaParserTagConverter;
import com.aim.core.resolver.javaparser.converter.JavaParserTagConverterRegistrar;
import com.aim.core.tag.DocTag;
import com.aim.core.utils.ClassMapperUtils;
import com.aim.core.utils.CommentUtils;
import com.aim.core.utils.AimUtil;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @AUTO 基于开源JavaParser实现的解析
 * @Author AIM
 * @DATE 2021/10/21
 */
public class JavaParserDocTagResolver implements Resolver {

	private Logger log = LoggerFactory.getLogger(JavaParserDocTagResolver.class);

	@Override
	public List<ApiModule> resolve(List<String> files) {
		//先缓存类文件信息
		for (String file : files) {
			try (FileInputStream in = new FileInputStream(file)) {
				CompilationUnit cu = JavaParser.parse(in);
				if (cu.getTypes().size() <= 0) {
					continue;
				}

				TypeDeclaration typeDeclaration = cu.getTypes().get(0);
				final Class<?> moduleType =
						Class.forName(cu.getPackageDeclaration().get().getNameAsString() + "." + typeDeclaration.getNameAsString());
				IgnoreApi ignoreApi = moduleType.getAnnotation(IgnoreApi.class);
				if (ignoreApi == null) {
					//缓存"包名+类名"跟对应的.java文件的位置映射关系
					ClassMapperUtils.put(moduleType.getName(), file);
					//缓存"类名"跟对应的.java文件的位置映射关系
					ClassMapperUtils.put(moduleType.getSimpleName(), file);
				}
			} catch (Exception e) {
				log.warn("读取文件失败:{}, {}", file, e.getMessage());
			}
		}

		List<ApiModule> apiModules = new LinkedList<>();

		for (String file : files) {
			try (FileInputStream in = new FileInputStream(file)) {
				CompilationUnit cu = JavaParser.parse(in);
				if (cu.getTypes().size() <= 0) {
					continue;
				}

				TypeDeclaration typeDeclaration = cu.getTypes().get(0);
				final Class<?> moduleType =
						Class.forName(cu.getPackageDeclaration().get().getNameAsString() + "." + typeDeclaration.getNameAsString());

				IgnoreApi ignoreApi = moduleType.getAnnotation(IgnoreApi.class);
				if (ignoreApi != null) {
					continue;
				}

				final ApiModule apiModule = new ApiModule();
				apiModule.setType(moduleType);
				if (typeDeclaration.getComment().isPresent()) {
					String commentText = CommentUtils.parseCommentText(typeDeclaration.getComment().get().getContent());
					commentText = commentText.split("\n")[0].split("\r")[0];
					apiModule.setComment(commentText);
				}

				new VoidVisitorAdapter<Void>() {
					@Override
					public void visit(MethodDeclaration m, Void arg) {
						Method method = parseToMenthod(moduleType, m);
						if (method == null) {
							log.warn("查找不到方法:{}.{}", moduleType.getSimpleName(), m.getNameAsString());
							return;
						}

						IgnoreApi ignoreApi = method.getAnnotation(IgnoreApi.class);
						if (ignoreApi != null || !m.getComment().isPresent()) {
							return;
						}

						List<String> comments = CommentUtils.asCommentList(AimUtil.defaultIfBlank(m.getComment().get().getContent(), ""));
						List<DocTag> docTagList = new ArrayList<>(comments.size());

						for (int i = 0; i < comments.size(); i++) {
							String c = comments.get(i);
							String tagType = CommentUtils.getTagType(c);
							if (AimUtil.isBlank(tagType)) {
								continue;
							}
							JavaParserTagConverter converter = JavaParserTagConverterRegistrar.getInstance().getConverter(tagType);
							DocTag docTag = converter.converter(c);
							if (docTag != null) {
								docTagList.add(docTag);
							} else {
								log.warn("识别不了:{}", c);
							}
						}

						ApiAction apiAction = new ApiAction();
						if (m.getComment().isPresent()) {
							apiAction.setComment(CommentUtils.parseCommentText(m.getComment().get().getContent()));
						}
						apiAction.setName(m.getNameAsString());
						apiAction.setDocTags(docTagList);
						apiAction.setMethod(method);
						apiAction.setMethodDeclaration(m);
						apiModule.getApiActions().add(apiAction);

						super.visit(m, arg);
					}
				}.visit(cu, null);

				apiModules.add(apiModule);

			} catch (Exception e) {
				log.warn("解析{}失败:{}", file, e.getMessage());
				continue;
			}
		}
		return apiModules;
	}

	/**
	 * 获取指定方法的所有入参类型,便于反射
	 *
	 * @param declaration
	 * @return
	 */
	private static Method parseToMenthod(Class clazz, MethodDeclaration declaration) {
		List<Parameter> parameters = declaration.getParameters();
		parameters = parameters == null ? new ArrayList<Parameter>(0) : parameters;
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			if (!m.getName().equals(declaration.getNameAsString())) {
				continue;
			}
			if (m.getParameterTypes().length != parameters.size()) {
				continue;
			}

			boolean b = true;

			for (int j = 0; j < m.getParameterTypes().length; j++) {
				Class<?> paramClass = m.getParameterTypes()[j];
				Type ptype = parameters.get(j).getType();
				if (ptype == null) {
					continue;
				}
				String paranTypeName = ptype.toString();
				int index = paranTypeName.lastIndexOf(".");
				if (index > 0) {
					paranTypeName = paranTypeName.substring(index + 1);
				}
				//处理泛型
				index = paranTypeName.indexOf("<");
				if (index > 0) {
					paranTypeName = paranTypeName.substring(0, index);
				}

				if (!paramClass.getSimpleName().equals(paranTypeName)) {
					b = false;
					break;
				}
			}
			if (b) {
				return m;
			}
		}
		return null;
	}
}
