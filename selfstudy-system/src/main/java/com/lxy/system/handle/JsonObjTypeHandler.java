package com.lxy.system.handle;


import com.lxy.common.util.JsonUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@MappedJdbcTypes(JdbcType.VARCHAR) // MySQL 中 JSON 映射为 VARCHAR 或 JSON 类型
@MappedTypes(Object.class)
public class JsonObjTypeHandler<T> extends BaseTypeHandler<T> {

    private final Class<T> type;

    public JsonObjTypeHandler(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        String json = JsonUtil.toJson(parameter); // 序列化为 JSON 字符串
        ps.setString(i, json); // 直接作为字符串设置进 SQL 参数中
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        if (json == null) {
            return null;
        }
        return JsonUtil.getTypeObj(json, type); // 反序列化为 Java 对象
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        if (json == null) {
            return null;
        }
        return JsonUtil.getTypeObj(json, type);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        if (json == null) {
            return null;
        }
        return JsonUtil.getTypeObj(json, type);
    }
}

