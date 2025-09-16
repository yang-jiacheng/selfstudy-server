package com.lxy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.system.po.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}
