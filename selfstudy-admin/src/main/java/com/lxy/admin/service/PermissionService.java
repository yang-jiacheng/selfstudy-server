package com.lxy.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.admin.po.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.common.domain.R;
import com.lxy.system.vo.PermissionTreeVO;

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
     * 修改菜单权限
     * @author jiacheng yang.
     * @since 2025/4/16 16:33
     */
    R<Object> saveOrUpdatePermission(Permission permission);

    /**
     * 获取权限树
     * @author jiacheng yang.
     * @since 2025/4/16 19:01
     */
    List<PermissionTreeVO> getPermissionTree();
}
