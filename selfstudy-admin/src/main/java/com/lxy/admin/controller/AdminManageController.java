package com.lxy.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.admin.security.util.AdminIdUtil;
import com.lxy.common.bo.R;
import com.lxy.common.po.AdminInfo;
import com.lxy.common.po.AdminRoleRelate;
import com.lxy.common.po.Role;
import com.lxy.common.service.AdminInfoService;
import com.lxy.common.service.AdminRoleRelateService;
import com.lxy.common.service.RolePermissionRelateService;
import com.lxy.common.service.RoleService;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.LayUiResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/10/08 20:11
 * @Version: 1.0
 */

@RequestMapping("/adminManage")
@Controller
@PreAuthorize("hasAuthority('/adminManage/adminList')")
public class AdminManageController {

    private final AdminInfoService adminInfoService;

    private final RoleService roleService;

    private final AdminRoleRelateService adminRoleRelateService;

    private final RolePermissionRelateService rolePermissionRelateService;

    @Autowired
    public AdminManageController(AdminInfoService adminInfoService, RoleService roleService, AdminRoleRelateService adminRoleRelateService,
                                 RolePermissionRelateService rolePermissionRelateService) {
        this.adminInfoService = adminInfoService;
        this.roleService = roleService;
        this.adminRoleRelateService = adminRoleRelateService;
        this.rolePermissionRelateService = rolePermissionRelateService;
    }

    @GetMapping("/adminList")
    public String adminList(){
        return "adminManage/adminList";
    }

    @GetMapping("/updateAdmin")
    public String updateAdmin(HttpServletRequest request){
        List<Role> roles = roleService.list();
        request.setAttribute("list",roles);
        return "adminManage/updateAdmin";
    }

    @RequestMapping(value = "/getAdminInfoList", produces = "application/json")
    @ResponseBody
    public LayUiResultVO getAdminInfoList(@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                   @RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit,
                                   @RequestParam(value = "username",required = false) String username){
        int userId = AdminIdUtil.getAdminId();
        Page<AdminInfo> pg = adminInfoService.getAdminInfoList(username, page, limit,userId);
        return new LayUiResultVO((int) pg.getTotal(), pg.getRecords());
    }

    @PostMapping(value ="/getAdminInfoById", produces = "application/json")
    @ResponseBody
    public R<Object> getAdminInfoById(@RequestParam Integer id){
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

    @PostMapping(value ="/addAdminInfo", produces = "application/json")
    @ResponseBody
    public R<Object> addAdminInfo(@RequestParam(value = "adminInfoJson") String adminInfoJson,
                              @RequestParam(value = "idsJson") String idsJson){
        AdminInfo adminInfo = JsonUtil.getTypeObj(adminInfoJson, AdminInfo.class);
        List<Integer> roleIds = JsonUtil.getListType(idsJson, Integer.class);
        if (adminInfo == null || CollUtil.isEmpty(roleIds)){
            return R.fail(-1,"数据有误！");
        }
        LambdaQueryWrapper<AdminInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminInfo::getPhone, adminInfo.getPhone());
        //是修改
        if (adminInfo.getId()!=null){
            wrapper.ne(AdminInfo::getId,adminInfo.getId());
            adminInfo.setUpdateTime(new Date());
            rolePermissionRelateService.removeCachePermissionInRole(roleIds);
        }
        AdminInfo one = adminInfoService.getOne(wrapper);
        if (one!=null){
            return R.fail(-1,"手机号已被使用！");
        }
        boolean flag = adminInfoService.saveOrUpdate(adminInfo);
        if (flag){
            Integer id = adminInfo.getId();
            //先把记录干掉
            adminRoleRelateService.remove(new LambdaUpdateWrapper<AdminRoleRelate>().eq(AdminRoleRelate::getAdminId,id));
            //再新增
            List<AdminRoleRelate> relates = new ArrayList<>(roleIds.size());
            AdminRoleRelate relate = null;
            for (Integer roleId : roleIds) {
                relate = new AdminRoleRelate();
                relate.setAdminId(id);
                relate.setRoleId(roleId);
                relates.add(relate);
            }
            adminRoleRelateService.saveBatch(relates);
        }
        return R.ok();
    }

    @PostMapping(value ="/disabledAdminInfo", produces = "application/json")
    @ResponseBody
    public R<Object> disabledAdminInfo(@RequestParam Integer id,@RequestParam Integer status){
        LambdaUpdateWrapper<AdminInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AdminInfo::getId,id).set(AdminInfo::getStatus,status);
        adminInfoService.update(wrapper);
        return R.ok();
    }

    @PostMapping(value ="/removeAdminInfoByIds", produces = "application/json")
    @ResponseBody
    public  R<Object>  removeAdminInfoByIds(@RequestParam(value = "ids") String ids){
        List<Integer> userIds = JsonUtil.getListType(ids, Integer.class);
        if (CollUtil.isEmpty(userIds)){
            return R.fail(-1,"请至少选择一个用户！");
        }
        adminInfoService.removeByIds(userIds);
        adminRoleRelateService.remove(new LambdaQueryWrapper<AdminRoleRelate>().in(AdminRoleRelate::getAdminId,userIds));
        return R.ok();
    }
}
