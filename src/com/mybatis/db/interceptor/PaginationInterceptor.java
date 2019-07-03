package com.mybatis.db.interceptor;

import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

import com.mybatis.db.dialect.Dialect;
import com.mybatis.db.dialect.MysqlDialect;
import com.mybatis.db.dialect.OracleDialect;



@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PaginationInterceptor implements Interceptor {

	private static final Log LOG = LogFactory
			.getLog(PaginationInterceptor.class);
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private Dialect dialect = null;

	@Override
	@SuppressWarnings("rawtypes")
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation
				.getTarget();
		MetaObject metaObject = MetaObject.forObject(statementHandler,
				DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

		if (LOG.isDebugEnabled()) {
			BoundSql boundSql = statementHandler.getBoundSql();
			String sql = boundSql.getSql().replaceAll("[\\t\\n\\r]", "");

			Object parameterObject = boundSql.getParameterObject();
			List<ParameterMapping> parameterMappings = boundSql
					.getParameterMappings();

			int parameterCount = parameterMappings.size();
			int whCount = count(sql, '?');

			StringBuffer sb = new StringBuffer();
			if (parameterCount == whCount) {
				if (parameterObject instanceof String) {
					if (whCount == 1) {
						sb.append(sql.replaceAll("\\?",
								"'" + parameterObject.toString() + "'"));
					}
				} else if (parameterObject instanceof HashMap) {
					if (!((HashMap) parameterObject).containsKey("list")) {
						int i = 0;
						int limit = 0;
						int base = 0;
						int length = sql.length();
						while ((limit = sql.indexOf('?', limit)) != -1) {
							sb.append(sql.substring(base, limit));
							String property = parameterMappings.get(i)
									.getProperty();
							sb.append(convertValue(((HashMap) parameterObject)
									.get(property)));
							i++;
							limit++;
							base = limit;
						}
						if (base < length) {
							sb.append(sql.substring(base));
						}
					} else {
						sb.append(sql);
					}
				}
			}
			LOG.debug("执行SQL：" + sb.toString());
		}

		RowBounds rowBounds = (RowBounds) metaObject
				.getValue("delegate.rowBounds");
		if (rowBounds == null || rowBounds.equals(RowBounds.DEFAULT)) {
			return invocation.proceed();
		}

		Configuration configuration = (Configuration) metaObject
				.getValue("delegate.configuration");
		Dialect.Type databaseType = null;

		try {
			databaseType = Dialect.Type.valueOf(configuration.getVariables()
					.getProperty("dialect").toUpperCase());
		} catch (Exception e) {
			LOG.error(e);
		}

		if (databaseType == null) {
			throw new Exception("没有在配置文件中设置属性：dialect的值！");
		}

		if (dialect == null) {
			switch (databaseType) {
			case ORACLE:
				dialect = new OracleDialect();
				break;
			case MYSQL:
				dialect = new MysqlDialect();
				break;
			}
		}

		String originalSql = (String) metaObject
				.getValue("delegate.boundSql.sql");
		String limitSql = dialect.getLimitString(originalSql,
				rowBounds.getOffset(), rowBounds.getLimit());
		metaObject.setValue("delegate.boundSql.sql", limitSql);
		metaObject.setValue("delegate.rowBounds.offset",
				RowBounds.NO_ROW_OFFSET);
		metaObject.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);

		return invocation.proceed();
	}

	public Dialect getDialect() {
		return dialect;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}

	private int count(String string, char c) {
		int count = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}

	private String convertValue(Object object) {
		StringBuffer sb = new StringBuffer();
		if (object instanceof String || object instanceof Date) {
			sb.append("'").append(object.toString()).append("'");
		} else {
			if (object == null) {
				sb.append("null");
			} else {
				sb.append(object.toString());
			}
		}
		return sb.toString();
	}

}