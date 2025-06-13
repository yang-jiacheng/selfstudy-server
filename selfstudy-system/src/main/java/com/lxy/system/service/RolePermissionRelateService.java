package com.lxy.system.service;

import com.lxy.system.po.RolePermissionRelate;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * @param roleId 角色id
     * @return 角色权限集合
     */
    List<RolePermissionRelate> getPermissionByRole(Integer roleId);

    /**
     * 移除缓存中角色对应的用户的登录状态
     */
    void removeCachePermissionInRole(List<Integer> roleIds);

}
