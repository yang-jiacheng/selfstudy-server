package com.lxy.admin.controller.auth;

import com.lxy.admin.po.Permission;
import com.lxy.admin.service.PermissionService;
import com.lxy.common.annotation.Log;
import com.lxy.common.domain.R;
import com.lxy.common.enums.LogBusinessType;
import com.lxy.common.enums.LogUserType;
import com.lxy.common.util.JsonUtil;
import com.lxy.system.dto.PageDTO;
import com.lxy.system.vo.PermissionTreeVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@RequestMapping("/permissionManage")
@RestController
//@PreAuthorize("hasAnyAuthority('/roleManage/permissionList')")
public class PermissionManageController {

    @Resource
    private PermissionService permissionService;

    /**
     * 获取菜单树
     * @author jiacheng yang.
     * @since 2025/4/20 1:16
     */
    @PostMapping(value = "/getPermissionTree", produces = "application/json")
    public R<List<PermissionTreeVO>> getPermissionTree(){
        List<PermissionTreeVO> tree = permissionService.getPermissionTree();
        return R.ok(tree);
    }

    @Log(title = "编辑菜单", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PostMapping(value = "/saveOrUpdatePermission", produces = "application/json")
    public R<Object> saveOrUpdatePermission(@RequestBody Permission permission){
        R<Object> r =permissionService.saveOrUpdatePermission(permission);
        return r;
    }

}
