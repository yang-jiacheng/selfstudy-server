package com.lxy.system.service;

import com.lxy.system.po.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.common.domain.R;
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

    /**
     * 根据 `id`  集合查询所有子级及自身
     * @author jiacheng yang.
     * @since 2025/5/16 16:06
     */
    List<Permission> getPermissionListAndChildren(Collection<Integer> ids);

    /**
     * 根据 `id`  集合查询所有父级及自身
     * @author jiacheng yang.
     * @since 2025/5/16 16:07
     */
    List<Permission> getPermissionListAndParent(Collection<Integer> ids);

    /**
     * 删除菜单
     * @author jiacheng yang.
     * @since 2025/5/16 16:07
     */
    void removePermission(Integer id);
}
