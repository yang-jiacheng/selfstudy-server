package com.lxy.common.mapper;

import com.lxy.common.po.Version;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 版本控制 Mapper 接口
 * </p>
 *
 * @author jiacheng yang.
 * @since 2025-02-24
 */
@Mapper
public interface VersionMapper extends BaseMapper<Version> {

}
