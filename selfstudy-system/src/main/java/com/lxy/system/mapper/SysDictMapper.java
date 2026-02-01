package com.lxy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.system.po.SysDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统字典表 Mapper 接口
 * </p>
 *
 * @author jiacheng yang.
 * @since 2026-02-01
 */
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysDict> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysDict> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysDict> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysDict> entities);

}
