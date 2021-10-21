package com.aim.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @AUTO 封装MAP
 * @Author AIM
 * @DATE 2021/10/21
 */
public class ClassMapperUtils {

	private static Map<String, String> classPath = new HashMap<>();

	public static void put(String name, String path) {
		classPath.put(name, path);
	}

	public static String getPath(String name) {
		return classPath.get(name);
	}
}
