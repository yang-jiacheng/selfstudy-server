package com.lxy.system.mapper;

import com.lxy.system.po.Version;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 版本控制 Mapper 接口
 * </p>
 *
 * @author jiacheng yang.
 * @since 2025-04-03
 */
@Mapper
public interface VersionMapper extends BaseMapper<Version> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Version> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Version> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Version> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Version> entities);

}
