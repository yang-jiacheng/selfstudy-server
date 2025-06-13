package com.lxy.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.admin.service.AuthService;
import com.lxy.common.dto.PageDTO;
import com.lxy.system.dto.RoleEditDTO;
import com.lxy.system.dto.RolePageDTO;
import com.lxy.system.po.AdminRoleRelate;
import com.lxy.system.po.Permission;
import com.lxy.system.po.Role;
import com.lxy.system.po.RolePermissionRelate;
import com.lxy.system.service.AdminRoleRelateService;
import com.lxy.system.service.PermissionService;
import com.lxy.system.service.RolePermissionRelateService;
import com.lxy.system.service.RoleService;
import com.lxy.common.domain.R;
import com.lxy.common.util.JsonUtil;

import com.lxy.system.vo.PermissionTreeVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 角色管理
 * @author jiacheng yang.
 * @since 2022/10/08 20:15
 * @version 1.0
 */

@RequestMapping("/roleManage")
@RestController
//@PreAuthorize("hasAnyAuthority('/roleManage/roleList')")
public class RoleManageController {

    @Resource
    private RoleService roleService;
    @Resource
    private AuthService authService;

    @PostMapping(value = "/hello", produces = "application/json")
    public R<Object> hello() {
        List<Role> roles = new ArrayList<>(50);
        Role role = null;
        Date now = new Date();
        for (int i = 0; i < 50; i++) {
            role = new Role();
            role.setName("角色" + i);
            role.setDescription("角色描述" + i);
            role.setCreateTime(new Date());
            role.setCreateTime(now);
            roles.add(role);
            now = DateUtil.offsetDay(now, 4);
            now = DateUtil.offsetHour(now, 1);
            now =DateUtil.offsetMinute(now, 3);
        }
        roleService.saveBatch(roles);
        return R.ok();
    }

    /**
     * 角色分页列表
     * @author jiacheng yang.
     * @since 2025/6/13 10:26
     */
    @PostMapping(value = "/getRolePageList", produces = "application/json")
    public R<Page<Role>> getRolePageList(@RequestBody RolePageDTO pageDTO){
        Page<Role> pg = roleService.getRolePageList(pageDTO);
        return R.ok(pg);
    }

    /**
     * 删除角色
     * @author jiacheng yang.
     * @since 2025/6/13 10:47
     */
    @PostMapping(value = "/removeRoleById", produces = "application/json")
    public R<Object> removeRoleById(@RequestParam("id") Integer id){
        authService.removeRoleById(id);
        return R.ok();
    }

    /**
     * 获取角色详情
     * @author jiacheng yang.
     * @since 2025/6/13 10:44
     */
    @PostMapping(value = "/getRoleById", produces = "application/json")
    public R<Map<String ,Object>> getRoleById(@RequestParam("id") Integer id){
        Map<String ,Object> map = authService.getRoleDetail(id);
        return R.ok(map);
    }

    /**
     * 编辑角色
     * @author jiacheng yang.
     * @since 2025/6/13 10:44
     */
    @PostMapping(value = "/addOrUpdateRole", produces = "application/json")
    public R<Integer> addOrUpdateRole(@RequestBody @Valid RoleEditDTO roleEditDTO){
        Integer roleId = authService.addOrUpdateRole(roleEditDTO);
        return R.ok(roleId);
    }


}
