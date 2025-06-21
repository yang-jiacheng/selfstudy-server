package com.lxy.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.admin.service.AuthService;
import com.lxy.common.annotation.Log;
import com.lxy.common.domain.R;
import com.lxy.common.dto.PageDTO;
import com.lxy.system.dto.AdminEditDTO;
import com.lxy.system.dto.AdminInfoPageDTO;
import com.lxy.system.dto.AdminStatusDTO;
import com.lxy.system.po.AdminInfo;
import com.lxy.system.po.AdminRoleRelate;
import com.lxy.system.po.Role;
import com.lxy.common.enums.LogBusinessType;
import com.lxy.common.enums.LogUserType;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.system.service.AdminInfoService;
import com.lxy.system.service.AdminRoleRelateService;
import com.lxy.system.service.RolePermissionRelateService;
import com.lxy.system.service.RoleService;
import com.lxy.common.util.JsonUtil;
import com.lxy.system.vo.LayUiResultVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/10/08 20:11
 * @version 1.0
 */

@RequestMapping("/adminManage")
@RestController
@PreAuthorize("hasAnyAuthority('systemManage','systemManage:adminManage')")
public class AdminManageController {

    @Resource
    private AdminInfoService adminInfoService;
    @Resource
    private AdminRoleRelateService adminRoleRelateService;
    @Resource
    private AuthService authService;


    /**
     * 获取后管用户
     * @author jiacheng yang.
     * @since 2025/6/17 19:13
     */
    @PostMapping(value = "/getAdminInfoPageList", produces = "application/json")
    public R<Page<AdminInfo>> getAdminInfoList(@RequestBody AdminInfoPageDTO pageDTO){
        int userId = UserIdUtil.getUserId();
        pageDTO.setUserId(userId);
        Page<AdminInfo> pg = adminInfoService.getAdminInfoPageList(pageDTO);
        return R.ok(pg);
    }

    /**
     * 获取后管用户根据id
     * @author jiacheng yang.
     * @since 2025/6/17 19:13
     */
    @PostMapping(value ="/getAdminInfoById", produces = "application/json")
    public R<Map<String ,Object>> getAdminInfoById(@RequestParam("id") Integer id){
        AdminInfo adminInfo = adminInfoService.getById(id);
        adminInfo.setPassword(null);
        List<AdminRoleRelate> roleRelates = adminRoleRelateService.list(new LambdaQueryWrapper<AdminRoleRelate>().eq(AdminRoleRelate::getAdminId, id));
        List<Integer> roles = new ArrayList<>();
        if (CollUtil.isNotEmpty(roleRelates)){
            roleRelates.forEach(roleRelate -> roles.add(roleRelate.getRoleId()));
        }
        Map<String ,Object> map = new HashMap<>(2);
        map.put("adminInfo",adminInfo);
        map.put("roles",roles);
        return R.ok(map);
    }

    @Log(title = "修改后管用户", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PostMapping(value ="/editAdminInfo", produces = "application/json")
    public R<Object> editAdminInfo(@RequestBody @Valid AdminEditDTO adminEditDTO){
        return authService.editAdminInfo(adminEditDTO);
    }

    @Log(title = "修改后管用户状态", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PostMapping(value ="/disabledAdminInfo", produces = "application/json")
    public R<Object> disabledAdminInfo(@RequestBody @Valid AdminStatusDTO dto){
        authService.disabledAdminInfo(dto);
        return R.ok();
    }

    @Log(title = "删除后管用户", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PostMapping(value ="/removeAdminInfoByIds", produces = "application/json")
    public  R<Object> removeAdminInfoByIds(@RequestBody @NotEmpty List<Integer> userIds){
        authService.removeAdminInfoByIds(userIds);
        return R.ok();
    }
}
