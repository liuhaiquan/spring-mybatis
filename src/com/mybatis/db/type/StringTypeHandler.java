package com.mybatis.db.type;

import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.mybatis.db.config.CharsetConfig;



public class StringTypeHandler extends BaseTypeHandler<String> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			String parameter, JdbcType jdbcType) throws SQLException {
		String value = null;
		try {
			if (JdbcType.DATE == jdbcType && parameter == "") {
				value = null;
			} else if (JdbcType.INTEGER == jdbcType && parameter == "") {
				value = null;
			} else if (JdbcType.DOUBLE == jdbcType && parameter == "") {
				value = null;
			} else if (JdbcType.DECIMAL == jdbcType && parameter == "") {
				value = null;
			} else {
				value = new String(
						parameter.getBytes(CharsetConfig.localCharset),
						CharsetConfig.dbCharset);
			}
		} catch (UnsupportedEncodingException e) {
			throw new SQLException(e);
		}
		ps.setString(i, value);
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		return getValue(rs.getString(columnName));
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		return getValue(rs.getString(columnIndex));
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return getValue(cs.getString(columnIndex));
	}

	private String getValue(String columnValue) throws SQLException {
		if (columnValue != null) {
			try {
				return new String(
						columnValue.getBytes(CharsetConfig.dbCharset),
						CharsetConfig.localCharset);
			} catch (UnsupportedEncodingException e) {
				throw new SQLException(e);
			}
		}
		return null;
	}

}