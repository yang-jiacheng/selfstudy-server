package com.lxy.common.mapper;

import com.lxy.common.po.RolePermissionRelate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    List<RolePermissionRelate> getPermissionByRole(Integer roleId);

    Set<Integer> getAdminIdsByRoles(@Param("ids") List<Integer> ids);

}
