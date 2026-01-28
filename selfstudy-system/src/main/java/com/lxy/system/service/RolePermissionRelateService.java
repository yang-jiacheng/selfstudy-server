package com.lxy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.system.po.RolePermissionRelate;

import java.util.List;

/**
 * <p>
 * 角色权限关联表 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
public interface RolePermissionRelateService extends IService<RolePermissionRelate> {
    /**
     * 获取角色权限
     *
     * @param roleId 角色id
     * @return 角色权限集合
     */
    List<RolePermissionRelate> getPermissionByRole(Long roleId);

    /**
     * 移除缓存中角色对应的用户的登录状态
     *
     * @param roleIds 角色id集合
     */
    void removeCachePermissionInRole(List<Long> roleIds);

    /**
     * 移除缓存中有该权限的登录状态
     *
     * @param permissionIds 权限id集合
     */
    void removeCachePermission(List<Long> permissionIds);

}
