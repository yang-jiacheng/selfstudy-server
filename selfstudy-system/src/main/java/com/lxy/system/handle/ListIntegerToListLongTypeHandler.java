package com.lxy.system.handle;

import cn.hutool.core.util.StrUtil;
import com.lxy.common.util.JsonUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/11/4 17:21
 */

public class ListIntegerToListLongTypeHandler extends BaseTypeHandler<List<Long>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType)
        throws SQLException {
        ps.setString(i, JsonUtil.toJson(parameter));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));
    }

    private List<Long> parseJson(String json) {
        if (StrUtil.isBlank(json)){
            return List.of();
        }
        return JsonUtil.getListType(json, Long.class);
    }

}
