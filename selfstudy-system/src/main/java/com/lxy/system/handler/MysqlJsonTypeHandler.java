package com.lxy.system.handler;

import com.lxy.common.util.JsonUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MySQL JSON 类型处理器
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2026/1/20 17:12
 */

@MappedTypes(Object.class)
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.LONGVARCHAR, JdbcType.OTHER // MySQL JSON
})
public class MysqlJsonTypeHandler<T> extends BaseTypeHandler<T> {

    private final Class<T> clazz;

    public MysqlJsonTypeHandler(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.clazz = clazz;
    }

    /**
     * MyBatis-Plus 3.5.6+ 支持的构造方法 （用于 autoResultMap + 泛型）
     */
    public MysqlJsonTypeHandler(Class<?> type, Field field) {
        this.clazz = (Class<T>)type;
    }

    /* ================== 写入数据库 ================== */

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {

        // MySQL JSON / VARCHAR 本质都是字符串
        ps.setString(i, JsonUtil.toJson(parameter));
    }

    /* ================== 从数据库读取 ================== */

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));
    }

    private T parseJson(String json) {
        return JsonUtil.getTypeObj(json, clazz);
    }

}
