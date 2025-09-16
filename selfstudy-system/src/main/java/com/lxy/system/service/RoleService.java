package com.lxy.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.system.dto.RolePageDTO;
import com.lxy.system.po.Role;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
public interface RoleService extends IService<Role> {

    /**
     * 角色分页列表
     *
     * @author jiacheng yang.
     * @since 2025/6/13 10:26
     */
    Page<Role> getRolePageList(RolePageDTO pageDTO);

}
