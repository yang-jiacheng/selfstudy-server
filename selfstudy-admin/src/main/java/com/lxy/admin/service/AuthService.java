package com.lxy.admin.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxy.common.domain.R;
import com.lxy.common.dto.AdminInfoUpdateDTO;
import com.lxy.common.dto.AdminEditDTO;
import com.lxy.common.dto.AdminStatusDTO;
import com.lxy.common.dto.RoleEditDTO;
import com.lxy.system.po.AdminInfo;
import com.lxy.system.po.AdminRoleRelate;
import com.lxy.system.po.Permission;
import com.lxy.system.po.Role;
import com.lxy.system.po.RolePermissionRelate;
import com.lxy.system.service.AdminInfoService;
import com.lxy.system.service.AdminRoleRelateService;
import com.lxy.system.service.PermissionService;
import com.lxy.system.service.RolePermissionRelateService;
import com.lxy.system.service.RoleService;
import com.lxy.common.vo.PermissionTreeVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Service
public class AuthService {

    @Resource
    private RoleService roleService;
    @Resource
    private RolePermissionRelateService rolePermissionRelateService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private AdminRoleRelateService adminRoleRelateService;
    @Resource
    private AdminInfoService adminInfoService;

    /**
     * 删除角色 及其关联关系
     *
     * @author jiacheng yang.
     * @since 2025/6/13 10:48
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeRoleById(Long id) {
        rolePermissionRelateService.removeCachePermissionInRole(Collections.singletonList(id));
        roleService.removeById(id);
        rolePermissionRelateService
            .remove(new LambdaUpdateWrapper<RolePermissionRelate>().eq(RolePermissionRelate::getRoleId, id));
        adminRoleRelateService.remove(new LambdaUpdateWrapper<AdminRoleRelate>().eq(AdminRoleRelate::getRoleId, id));
    }

    /**
     * 获取角色和权限
     *
     * @author jiacheng yang.
     * @since 2025/6/13 10:50
     */
    public Map<String, Object> getRoleDetail(Long id) {
        Role role = roleService.getById(id);
        // 角色权限
        List<Permission> rolePermission = permissionService.getRolePermission(id);
        List<Long> ids = rolePermission.stream().map(Permission::getId).toList();
        // 所有权限
        List<PermissionTreeVO> permissions = permissionService.getPermissionTree();
        Map<String, Object> map = new HashMap<>(3);
        map.put("role", role);
        map.put("allPermission", permissions);
        map.put("rolePermission", ids);
        return map;
    }

    /**
     * 编辑 角色
     *
     * @return 角色id
     * @author jiacheng yang.
     * @since 2025/6/13 10:54
     */
    @Transactional(rollbackFor = Exception.class)
    public Long addOrUpdateRole(RoleEditDTO roleEditDTO) {
        List<Long> permissionIds = roleEditDTO.getPermissionIds();
        Role role = new Role();
        BeanUtil.copyProperties(roleEditDTO, role);

        // 移除缓存中角色对应的用户的登录状态
        if (role.getId() != null) {
            role.setUpdateTime(new Date());
            rolePermissionRelateService.removeCachePermissionInRole(Collections.singletonList(role.getId()));
        }

        roleService.saveOrUpdate(role);

        // 先删
        Long roleId = role.getId();
        rolePermissionRelateService
            .remove(new LambdaUpdateWrapper<RolePermissionRelate>().eq(RolePermissionRelate::getRoleId, roleId));
        // 再新增
        List<RolePermissionRelate> list = new ArrayList<>(permissionIds.size());
        RolePermissionRelate relate = null;
        for (Long id : permissionIds) {
            relate = new RolePermissionRelate();
            relate.setRoleId(roleId);
            relate.setPermissionId(id);
            list.add(relate);
        }
        rolePermissionRelateService.saveBatch(list);

        return roleId;
    }

    /**
     * 修改后管用户
     *
     * @author jiacheng yang.
     * @since 2025/6/17 19:25
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Object> editAdminInfo(AdminEditDTO adminEditDTO) {
        AdminInfoUpdateDTO adminInfoDTO = adminEditDTO.getAdminInfo();
        AdminInfo adminInfo = BeanUtil.copyProperties(adminInfoDTO, AdminInfo.class);
        List<Long> roleIds = adminEditDTO.getRoleIds();
        LambdaQueryWrapper<AdminInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.nested(w -> w.eq(AdminInfo::getPhone, adminInfo.getPhone()).or().eq(AdminInfo::getUsername,
            adminInfo.getUsername()));
        // 是修改
        if (adminInfo.getId() != null) {
            wrapper.ne(AdminInfo::getId, adminInfo.getId());
            adminInfo.setUpdateTime(new Date());
            rolePermissionRelateService.removeCachePermissionInRole(roleIds);
        }

        AdminInfo one = adminInfoService.getOne(wrapper);
        if (one != null) {
            return R.fail("用户名或手机号已被使用！");
        }
        adminInfoService.updateAdmin(adminInfo);
        Long id = adminInfo.getId();
        // 先把记录干掉
        adminRoleRelateService.remove(new LambdaUpdateWrapper<AdminRoleRelate>().eq(AdminRoleRelate::getAdminId, id));
        // 再新增
        List<AdminRoleRelate> relates = new ArrayList<>(roleIds.size());
        AdminRoleRelate relate = null;
        for (Long roleId : roleIds) {
            relate = new AdminRoleRelate();
            relate.setAdminId(id);
            relate.setRoleId(roleId);
            relates.add(relate);
        }
        adminRoleRelateService.saveBatch(relates);
        return R.ok();
    }

    /**
     * 修改后管用户状态
     *
     * @author jiacheng yang.
     * @since 2025/6/17 19:34
     */
    @Transactional(rollbackFor = Exception.class)
    public void disabledAdminInfo(AdminStatusDTO dto) {
        LambdaUpdateWrapper<AdminInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AdminInfo::getId, dto.getId()).set(AdminInfo::getStatus, dto.getStatus());
        adminInfoService.update(wrapper);
        adminInfoService.removeCachePermissionInAdminIds(Collections.singletonList(dto.getId()));
        adminInfoService.updateAdminInfoCache(dto.getId());
    }

    /**
     * 删除后管用户
     *
     * @author jiacheng yang.
     * @since 2025/6/17 19:34
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeAdminInfoByIds(List<Long> userIds) {
        adminInfoService.removeByIds(userIds);
        adminRoleRelateService
            .remove(new LambdaQueryWrapper<AdminRoleRelate>().in(AdminRoleRelate::getAdminId, userIds));
        adminInfoService.removeCachePermissionInAdminIds(userIds);
    }

}
