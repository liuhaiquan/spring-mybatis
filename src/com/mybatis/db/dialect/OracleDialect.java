package com.mybatis.db.dialect;

public class OracleDialect extends Dialect {

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from (select t.*, rownum rn from (");
        sb.append(sql);
        sb.append(") t) where rn > ");
        sb.append((offset - 1) * limit);
        sb.append(" and rn <= ");
        sb.append(offset * limit);
        return sb.toString();
    }

}