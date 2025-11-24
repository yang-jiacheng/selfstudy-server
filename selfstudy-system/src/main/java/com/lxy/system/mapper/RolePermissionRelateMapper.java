package com.lxy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.system.po.RolePermissionRelate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色权限关联表 Mapper 接口
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
@Mapper
public interface RolePermissionRelateMapper extends BaseMapper<RolePermissionRelate> {
    List<RolePermissionRelate> getPermissionByRole(Long roleId);

    Set<Long> getAdminIdsByRoles(@Param("ids") List<Long> ids);

}
