package com.lxy.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.common.po.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 获取角色权限
     * @param roleId 角色id
     * @return 权限集合
     */
    List<Permission> getRolePermission(Integer roleId);

    /**
     * 条件查询权限列表
     * @param urlCode url代码
     * @param page 当前页
     * @param limit 每页数量
     * @return 权限
     */
    Page<Permission> getPermissionList(String urlCode, Integer page, Integer limit);

}
