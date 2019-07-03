package com.mybatis.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;

public class MybatisDao {

	private SqlSessionTemplate sqlSessionTemplate;

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public void insert(String key, Object parameter) {
		sqlSessionTemplate.insert(key, parameter);
	}

	public void delete(String key, Object parameter) {
		sqlSessionTemplate.delete(key, parameter);
	}

	public void update(String key, Object parameter) {
		sqlSessionTemplate.update(key, parameter);
	}

	public Object get(String key, Object parameter) {
		return sqlSessionTemplate.selectOne(key, parameter);
	}

	public List<Object> list(String key, Object parameter) {
		return sqlSessionTemplate.selectList(key, parameter);
	}

	public int count(String key, Object parameter) {
		return (Integer) sqlSessionTemplate.selectOne(key, parameter);
	}

	public List<Object> list(String key, Object parameter, int pageIndex,
			int pageSize) {
		RowBounds rowBounds = new RowBounds(pageIndex, pageSize);
		return sqlSessionTemplate.selectList(key, parameter, rowBounds);
	}

	public String getSql(String key) {
		return sqlSessionTemplate.getConfiguration().getMappedStatement(key)
				.getBoundSql(null).getSql();
	}
}