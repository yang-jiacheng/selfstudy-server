package com.lxy.admin.controller;

import com.lxy.common.annotation.Log;
import com.lxy.common.enums.dict.LogBusinessType;
import com.lxy.common.enums.dict.LogUserType;
import com.lxy.common.model.R;
import com.lxy.system.po.Permission;
import com.lxy.system.service.PermissionService;
import com.lxy.system.vo.PermissionTreeVO;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@RequestMapping("/permissionManage")
@RestController
@PreAuthorize("hasAnyAuthority('systemManage','systemManage:permissionManage')")
public class PermissionManageController {

    @Resource
    private PermissionService permissionService;

    @PostMapping(value = "/getPermissionList", produces = "application/json")
    public R<List<Permission>> getPermissionList() {
        List<Permission> list = permissionService.list();
        return R.ok(list);
    }

    /**
     * 获取权限树
     *
     * @author jiacheng yang.
     * @since 2025/4/20 1:16
     */
    @PostMapping(value = "/getPermissionTree", produces = "application/json")
    public R<List<PermissionTreeVO>> getPermissionTree() {
        List<PermissionTreeVO> tree = permissionService.getPermissionTree();
        return R.ok(tree);
    }

    @Log(title = "编辑权限", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PostMapping(value = "/saveOrUpdatePermission", produces = "application/json")
    public R<Object> saveOrUpdatePermission(@RequestBody Permission permission) {
        R<Object> r = permissionService.saveOrUpdatePermission(permission);
        return r;
    }

    @Log(title = "删除权限", businessType = LogBusinessType.DELETE, userType = LogUserType.ADMIN)
    @PostMapping(value = "/removePermission", produces = "application/json")
    public R<Object> removePermission(@RequestParam("id") Long id) {
        permissionService.removePermission(id);
        return R.ok();
    }

}
