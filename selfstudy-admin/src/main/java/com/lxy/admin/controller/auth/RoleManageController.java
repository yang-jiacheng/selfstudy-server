package com.lxy.admin.controller.auth;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.admin.po.AdminRoleRelate;
import com.lxy.admin.po.Permission;
import com.lxy.admin.po.Role;
import com.lxy.admin.po.RolePermissionRelate;
import com.lxy.admin.service.AdminRoleRelateService;
import com.lxy.admin.service.PermissionService;
import com.lxy.admin.service.RolePermissionRelateService;
import com.lxy.admin.service.RoleService;
import com.lxy.common.domain.R;
import com.lxy.common.util.JsonUtil;
import com.lxy.system.vo.LayUiResultVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/10/08 20:15
 * @version 1.0
 */

@RequestMapping("/roleManage")
@Controller
@PreAuthorize("hasAnyAuthority('/roleManage/roleList','/roleManage/permissionList')")
public class RoleManageController {

    private final RoleService roleService;

    private final RolePermissionRelateService rolePermissionRelateService;

    private final PermissionService permissionService;

    private final AdminRoleRelateService adminRoleRelateService;


    @Autowired
    public RoleManageController(RoleService roleService, RolePermissionRelateService rolePermissionRelateService, PermissionService permissionService, AdminRoleRelateService adminRoleRelateService) {
        this.roleService = roleService;
        this.rolePermissionRelateService = rolePermissionRelateService;
        this.permissionService = permissionService;
        this.adminRoleRelateService = adminRoleRelateService;
    }

    @GetMapping("/roleList")
    public String roleList(){
        return "adminManage/roleList";
    }

    @GetMapping("/updateRole")
    public String updateRole(){
        return "adminManage/updateRole";
    }

    @GetMapping("/permissionList")
    public String permissionList(){
        return "adminManage/permissionList";
    }

    @GetMapping("/updatePermission")
    public String updatePermission(){
        return "adminManage/updatePermission";
    }

    @PostMapping(value = "/getRoleList", produces = "application/json")
    @ResponseBody
    public String getRoleList(@RequestParam(value = "page",required = false) Integer page,
                              @RequestParam(value = "limit",required = false) Integer limit){
        if (page!=null && limit!=null){
            Page<Role> pg = new Page<>(page,limit);
            pg.addOrder(OrderItem.desc("id"));
            pg = roleService.page(pg);
            return JsonUtil.toJson(new LayUiResultVO((int) pg.getTotal(), pg.getRecords()));
        }
        List<Role> roles = roleService.list();
        return JsonUtil.toJson(R.ok(roles));
    }

    @PostMapping(value = "/removeRoleById", produces = "application/json")
    @ResponseBody
    public R<Object> removeRoleById(@RequestParam("id") Integer id){
        rolePermissionRelateService.removeCachePermissionInRole(Collections.singletonList(id));
        roleService.removeById(id);
        rolePermissionRelateService.remove(new LambdaUpdateWrapper<RolePermissionRelate>().eq(RolePermissionRelate::getRoleId,id));
        adminRoleRelateService.remove(new LambdaUpdateWrapper<AdminRoleRelate>().eq(AdminRoleRelate::getRoleId,id));
        return R.ok();
    }

    @PostMapping(value = "/getRoleById", produces = "application/json")
    @ResponseBody
    public R<Map<String ,Object>> getRoleById(@RequestParam("id") Integer id){
        Role role = roleService.getById(id);
        //角色权限
        List<Permission> rolePermission=permissionService.getRolePermission(id);
        //所有权限
        List<Permission> permissions=permissionService.list();
        Map<String ,Object> map=new HashMap<>(3);
        map.put("role",role);
        map.put("allPermission",permissions);
        map.put("rolePermission",rolePermission);
        return R.ok(map);
    }

    @PostMapping(value = "/addOrUpdateRole", produces = "application/json")
    @ResponseBody
    public R<Integer> addOrUpdateRole(@RequestParam(value = "roleJson") String roleJson,
                                 @RequestParam(value = "idsJson") String idsJson){
        Role role = JsonUtil.getTypeObj(roleJson, Role.class);
        List<Integer> ids = JsonUtil.getListType(idsJson, Integer.class);
        if (role == null || CollUtil.isEmpty(ids)){
            return R.fail(-1,"数据有误！");
        }
        if (role.getId()!=null){
            role.setUpdateTime(new Date());
            rolePermissionRelateService.removeCachePermissionInRole(Collections.singletonList(role.getId()));
        }

        boolean flag = roleService.saveOrUpdate(role);
        if (flag){
            //先删
            Integer roleId = role.getId();
            rolePermissionRelateService.remove(new LambdaUpdateWrapper<RolePermissionRelate>().eq(RolePermissionRelate::getRoleId,roleId));
            //再新增
            List<RolePermissionRelate> list = new ArrayList<>(ids.size());
            RolePermissionRelate relate = null;
            for (Integer id : ids) {
                relate = new RolePermissionRelate();
                relate.setRoleId(roleId);
                relate.setPermissionId(id);
                list.add(relate);
            }
            rolePermissionRelateService.saveBatch(list);
        }
        return R.ok(role.getId());
    }

    @PostMapping(value = "/getPermissionList", produces = "application/json")
    @ResponseBody
    public Map<String ,Object> getPermissionList(@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                    @RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit,
                                    @RequestParam(value = "urlCode",required = false) String urlCode){
        Page<Permission> pg = permissionService.getPermissionList(urlCode, page, limit);
        Map<String ,Object> map = new HashMap<>(4);
        map.put("code",0);
        map.put("msg","调用成功");
        map.put("count",pg.getTotal());
        map.put("data",pg.getRecords());
        return map;
    }

    @PostMapping(value = "/getPermissionById", produces = "application/json")
    @ResponseBody
    public R<Permission> getPermissionById(@RequestParam("id") Integer id){
        Permission permission = permissionService.getById(id);
        return R.ok(permission);
    }

    @PostMapping(value = "/updatePermission", produces = "application/json")
    @ResponseBody
    public R<Object> updatePermission(@RequestParam(value = "permissionJson") String permissionJson){
        Permission permission = JsonUtil.getTypeObj(permissionJson, Permission.class);
        if (permission == null){
            return R.fail(-1,"数据有误！");
        }
        if (permission.getId()!=null){
            permission.setUpdateTime(new Date());
        }
        permissionService.saveOrUpdate(permission);
        return R.ok();
    }


}
