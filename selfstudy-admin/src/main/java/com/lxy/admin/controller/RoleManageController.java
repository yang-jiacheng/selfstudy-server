package com.lxy.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.admin.service.AuthService;
import com.lxy.common.annotation.Log;
import com.lxy.common.domain.CollResult;
import com.lxy.common.dto.PageDTO;
import com.lxy.common.enums.LogBusinessType;
import com.lxy.common.enums.LogUserType;
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

    @PostMapping(value = "/getRoleRecords", produces = "application/json")
    public R<CollResult<Role>> getRoleRecords(){
        return R.ok(new CollResult<>(roleService.list()));
    }

    /**
     * 删除角色
     * @author jiacheng yang.
     * @since 2025/6/13 10:47
     */
    @Log(title = "删除角色", businessType = LogBusinessType.DELETE, userType = LogUserType.ADMIN)
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
    @Log(title = "编辑角色", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PostMapping(value = "/addOrUpdateRole", produces = "application/json")
    public R<Integer> addOrUpdateRole(@RequestBody @Valid RoleEditDTO roleEditDTO){
        Integer roleId = authService.addOrUpdateRole(roleEditDTO);
        return R.ok(roleId);
    }



}
