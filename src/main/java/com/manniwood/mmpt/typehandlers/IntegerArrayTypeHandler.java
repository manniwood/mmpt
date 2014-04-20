package com.manniwood.mmpt.typehandlers;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(Integer[].class)
public class IntegerArrayTypeHandler extends BaseTypeHandler<Integer[]> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
            Integer[] parameter, JdbcType jdbcType) throws SQLException {
        Connection c = ps.getConnection();
        Array inArray = c.createArrayOf("int", parameter);
        ps.setArray(1, inArray);
    }

    @Override
    public Integer[] getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        Array outputArray = rs.getArray(columnName);
        if (outputArray == null) {
            return null;
        }
        return (Integer[])outputArray.getArray();
    }

    @Override
    public Integer[] getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        Array outputArray = rs.getArray(columnIndex);
        if (outputArray == null) {
            return null;
        }
        return (Integer[])outputArray.getArray();
    }

    @Override
    public Integer[] getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        Array outputArray = cs.getArray(columnIndex);
        if (outputArray == null) {
            return null;
        }
        return (Integer[])outputArray.getArray();
    }
}