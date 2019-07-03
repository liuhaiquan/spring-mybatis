package com.mybatis.db.dialect;

public class MysqlDialect extends Dialect {

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		StringBuffer sb = new StringBuffer();
		sb.append(sql);
		sb.append(" limit ");
		sb.append((offset - 1) * limit);
		sb.append(",");
		sb.append(limit);
		return sb.toString();
	}

}