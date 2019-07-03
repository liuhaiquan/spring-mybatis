package com.mybatis.db.dialect;

public class SQLServerDialect extends Dialect {
	static int getLastIndexOfOrderBy(String sql) {
		return sql.toLowerCase().lastIndexOf("order by ");
	}

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		if (offset == 0) {
			return sql;
		} else {
			int lastIndexOfOrderBy = getLastIndexOfOrderBy(sql);
			int indexOfFrom = sql.toLowerCase().indexOf("from");
			String orderby;
			String selectFromTableAndWhere;
			if (lastIndexOfOrderBy < 0) {
				orderby = "order by getdate()";
				selectFromTableAndWhere = sql.substring(indexOfFrom,
						sql.length());
			} else {
				orderby = sql.substring(lastIndexOfOrderBy, sql.length());
				selectFromTableAndWhere = sql.substring(indexOfFrom,
						lastIndexOfOrderBy);
			}
			String selectFld = sql.substring(0, indexOfFrom);
			StringBuffer sb = new StringBuffer(sql.length() + 100);
			sb.append("select * from (").append(selectFld)
					.append(",ROW_NUMBER() OVER(").append(orderby)
					.append(") as _page_row_num ")
					.append(selectFromTableAndWhere).append(" ) temp ")
					.append(" where  _page_row_num BETWEEN  ")
					.append(offset + 1).append(" and ").append(limit);
			return sb.toString();
		}
	}
}