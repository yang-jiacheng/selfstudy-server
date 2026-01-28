package com.lxy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.common.model.R;
import com.lxy.system.po.Permission;
import com.lxy.system.vo.PermissionTreeVO;

import java.util.Collection;
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
     *
     * @param roleId 角色id
     * @return 权限集合
     */
    List<Permission> getRolePermission(Long roleId);

    /**
     * 修改菜单权限
     *
     * @author jiacheng yang.
     * @since 2025/4/16 16:33
     */
    R<Object> saveOrUpdatePermission(Permission permission);

    /**
     * 获取权限树
     *
     * @author jiacheng yang.
     * @since 2025/4/16 19:01
     */
    List<PermissionTreeVO> getPermissionTree();

    /**
     * 根据 `id` 集合查询所有子级及自身
     *
     * @author jiacheng yang.
     * @since 2025/5/16 16:06
     */
    List<Permission> getPermissionListAndChildren(Collection<Long> ids);

    /**
     * 根据 `id` 集合查询所有父级及自身
     *
     * @author jiacheng yang.
     * @since 2025/5/16 16:07
     */
    List<Permission> getPermissionListAndParent(Collection<Long> ids);

    /**
     * 删除权限
     *
     * @author jiacheng yang.
     * @since 2025/5/16 16:07
     */
    void removePermission(Long id);

    /**
     * 根据登录用户获取权限
     *
     * @author jiacheng yang.
     * @since 2025/6/21 18:33
     */
    List<PermissionTreeVO> getMinePermissionTree(Long userId);
}
