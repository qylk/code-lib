package com.qylk.code.utils.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {

	public static String md5Encode(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] result = md.digest(str.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				int n = b & 0xff;
				String bs = Integer.toHexString(n);
				if (bs.length() == 1)
					sb.append('0');
				else {
					sb.append(bs);
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();// this may never happen
		}
		return null;
	}

	/**
	 * MD5����
	 * 
	 * @param str
	 *            ������ַ�
	 * @param repeat
	 *            �ظ����ܴ���
	 * @return
	 */
	public static String md5Encode(String str, int repeat) {
		String result = str;
		while (repeat > 0) {
			result = md5Encode(result);
			repeat--;
		}
		return str;
	}
}
