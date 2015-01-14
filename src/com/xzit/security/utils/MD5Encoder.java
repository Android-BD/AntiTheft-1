package com.xzit.security.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密 对用户输入的安全密码进行加密之后再进行存储
 * 
 * @author lenovo
 * 
 */
public class MD5Encoder {
	public static String encode(String pwd) {
		try {
			/**
			 * MessageDigest 类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA
			 * 算法。信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
			 */
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] bytes = messageDigest.digest(pwd.getBytes());
			
			StringBuffer sb = new StringBuffer();
			String tmp;
			for (int i = 0; i < bytes.length; i++) {
				tmp = Integer.toHexString(0xff & bytes[i]);
				if (tmp.length() == 1) {
					sb.append("0" + tmp);
				} else {
					sb.append(tmp);
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有这个加密算法" + e);
		}
	}

}
