/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.bluetron.nb.common.util.codec;

import cn.hutool.crypto.SecureUtil;

import java.io.InputStream;

public class Md5Utils {

	// for 100% code coverage
	private Md5Utils() {
	}

	/**
	 * 对输入字符串进行md5散列.
	 * 
	 * @param input 加密字符串
	 */
	public static String md5(String input) {
		return SecureUtil.md5(input);
	}

	/**
	 * 	对文件进行md5散列
	 * @param fis
	 * @return
	 * @throws Exception
	 */
	public static String md5(InputStream fis) throws Exception {
		return SecureUtil.md5(fis);
	}

}
