package com.mybatis.db.config;


import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CharsetConfig {

	private static final Log LOG = LogFactory.getLog(CharsetConfig.class);
	public static String localCharset;
	public static String dbCharset;

	public void setLocalCharset(String localCharset) {
		CharsetConfig.localCharset = localCharset;
	}

	public void setDbCharset(String dbCharset) {
		CharsetConfig.dbCharset = dbCharset;
	}

	public static String local2db(String string) {
		if (StringUtils.isBlank(string)) {
			return string;
		}
		if (localCharset.equals(dbCharset)) {
			return string;
		}
		try {
			return new String(string.getBytes(localCharset), dbCharset);
		} catch (UnsupportedEncodingException e) {
			LOG.error("字符串由本地字符集转换成数据库字符集时出现异常：", e);
		}
		return string;
	}

	public static String db2local(String string) {
		if (StringUtils.isBlank(string)) {
			return string;
		}
		if (localCharset.equals(dbCharset)) {
			return string;
		}
		try {
			return new String(string.getBytes(dbCharset), localCharset);
		} catch (UnsupportedEncodingException e) {
			LOG.error("字符串由数据库字符集转换成本地字符集时出现异常：", e);
		}
		return string;
	}

}