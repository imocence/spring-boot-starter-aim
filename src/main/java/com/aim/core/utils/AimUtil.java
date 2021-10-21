package com.aim.core.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @AUTO 对象工具类
 * @Author AIM
 * @DATE 2021/10/21
 */
public class AimUtil {

	public static final String EMPTY = "";
	public static final String CHARSET = "utf-8";

	/**
	 * 判断对象或对象数组中每一个对象是否不为空
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null || "".equals(obj))
			return true;

		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection<?>) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map<?, ?>) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}

	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs != null && (strLen = cs.length()) != 0) {
			for (int i = 0; i < strLen; ++i) {
				if (!Character.isWhitespace(cs.charAt(i))) {
					return false;
				}
			}
			return true;
		} else {
			return true;
		}
	}

	public static boolean isNotBlank(CharSequence cs) {
		return !isBlank(cs);
	}

	public static <T extends CharSequence> T defaultIfBlank(T str, T defaultStr) {
		return isBlank(str) ? defaultStr : str;
	}

	public static String defaultString(String str, String defaultStr) {
		return str == null ? defaultStr : str;
	}
}
