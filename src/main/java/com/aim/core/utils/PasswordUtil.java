package com.aim.core.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @Author AIM
 * @Des 加盐 sha256()
 * @DATE 2021/11/22
 */
public class PasswordUtil {
	/** 字母数字 */
	private static String ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	/** 符号字母数字 */
	private static String APOSTROPHE = "!@#$%^&*()_+{}[]|-=~" + ALPHANUMERIC;

	public static void main(String[] args) {
		String pwd = getPasswd(10);
		String salt = getSalt(10);
		System.out.println(getSHA256StrJava(pwd + salt));
	}

	/**
	 * 生成指定长度密码
	 */
	public static String getPasswd(int length) {
		return getSalt(8, ALPHANUMERIC);
	}

	/**
	 * 生成指定长度盐值
	 */
	public static String getSalt(int length) {
		return getSalt(8, APOSTROPHE);
	}

	/**
	 * 生成密码需要的盐
	 * @param length 生成长度
	 * @param dicts 字典库字符串
	 */
	public static String getSalt(int length, String dicts) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			sb.append(dicts.charAt(number));
		}
		return sb.toString();
	}

	public static String getSHA256StrJava(String str) {
		MessageDigest messageDigest;
		String encodeStr = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes("UTF-8"));
			encodeStr = byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodeStr;
	}

	private static String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {//1得到一位的进行补0操作
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}
}
