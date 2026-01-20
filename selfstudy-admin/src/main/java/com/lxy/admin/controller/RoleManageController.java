package com.lxy.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.admin.service.AuthService;
import com.lxy.common.annotation.Log;
import com.lxy.common.model.CollResult;
import com.lxy.common.model.R;
import com.lxy.common.enums.LogBusinessType;
import com.lxy.common.enums.LogUserType;
import com.lxy.common.vo.LabelValueVO;
import com.lxy.common.dto.RoleEditDTO;
import com.lxy.common.dto.RolePageDTO;
import com.lxy.system.po.Role;
import com.lxy.system.service.RoleService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/10/08 20:15
 */

@RequestMapping("/roleManage")
@RestController
@PreAuthorize("hasAnyAuthority('systemManage','systemManage:roleManage')")
public class RoleManageController {

    @Resource
    private RoleService roleService;
    @Resource
    private AuthService authService;

    /**
     * 角色分页列表
     *
     * @author jiacheng yang.
     * @since 2025/6/13 10:26
     */
    @PostMapping(value = "/getRolePageList", produces = "application/json")
    public R<Page<Role>> getRolePageList(@RequestBody RolePageDTO pageDTO) {
        Page<Role> pg = roleService.getRolePageList(pageDTO);
        return R.ok(pg);
    }

    @PostMapping(value = "/getRoleRecords", produces = "application/json")
    public R<CollResult<LabelValueVO>> getRoleRecords() {
        List<Role> list = roleService.list();
        List<LabelValueVO> vos = new ArrayList<>();
        for (Role role : list) {
            vos.add(new LabelValueVO(role.getId(), role.getName()));
        }
        return R.ok(new CollResult<>(vos));
    }

    /**
     * 删除角色
     *
     * @author jiacheng yang.
     * @since 2025/6/13 10:47
     */
    @Log(title = "删除角色", businessType = LogBusinessType.DELETE, userType = LogUserType.ADMIN)
    @PostMapping(value = "/removeRoleById", produces = "application/json")
    public R<Object> removeRoleById(@RequestParam("id") Long id) {
        authService.removeRoleById(id);
        return R.ok();
    }

    /**
     * 获取角色详情
     *
     * @author jiacheng yang.
     * @since 2025/6/13 10:44
     */
    @PostMapping(value = "/getRoleById", produces = "application/json")
    public R<Map<String, Object>> getRoleById(@RequestParam("id") Long id) {
        Map<String, Object> map = authService.getRoleDetail(id);
        return R.ok(map);
    }

    /**
     * 编辑角色
     *
     * @author jiacheng yang.
     * @since 2025/6/13 10:44
     */
    @Log(title = "编辑角色", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PostMapping(value = "/addOrUpdateRole", produces = "application/json")
    public R<Long> addOrUpdateRole(@RequestBody @Valid RoleEditDTO roleEditDTO) {
        Long roleId = authService.addOrUpdateRole(roleEditDTO);
        return R.ok(roleId);
    }

}
