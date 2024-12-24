package com.lxy.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.common.po.AdminRoleRelate;
import com.lxy.common.po.Permission;
import com.lxy.common.po.Role;
import com.lxy.common.po.RolePermissionRelate;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.service.AdminRoleRelateService;
import com.lxy.common.service.PermissionService;
import com.lxy.common.service.RolePermissionRelateService;
import com.lxy.common.service.RoleService;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.LayUiResultVO;
import com.lxy.common.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/10/08 20:15
 * @Version: 1.0
 */

@RequestMapping("/roleManage")
@Controller
@Api(tags = "角色、权限管理")
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

    @ApiOperation(value = "角色管理页面", produces = "application/json", notes = "jiacheng yang.")
    @GetMapping("/roleList")
    public String roleList(){
        return "adminManage/roleList";
    }

    @ApiOperation(value = "编辑角色管理页面", produces = "application/json", notes = "jiacheng yang.")
    @GetMapping("/updateRole")
    public String updateRole(){
        return "adminManage/updateRole";
    }

    @ApiOperation(value = "权限管理页面", produces = "application/json", notes = "jiacheng yang.")
    @GetMapping("/permissionList")
    public String permissionList(){
        return "adminManage/permissionList";
    }

    @ApiOperation(value = "编辑权限管理页面", produces = "application/json", notes = "jiacheng yang.")
    @GetMapping("/updatePermission")
    public String updatePermission(){
        return "adminManage/updatePermission";
    }

    @ApiOperation(value = "获取角色列表", notes = "jiacheng yang.")
    @PostMapping(value = "/getRoleList", produces = "application/json")
    @ResponseBody
    public String getRoleList(@ApiParam(value = "当前页")@RequestParam(value = "page",required = false) Integer page,
                              @ApiParam(value = "每页数量")@RequestParam(value = "limit",required = false) Integer limit){
        if (page!=null && limit!=null){
            Page<Role> pg = new Page<>(page,limit);
            pg.addOrder(OrderItem.desc("id"));
            pg = roleService.page(pg);
            return JsonUtil.toJson(new LayUiResultVO((int) pg.getTotal(), pg.getRecords()));
        }
        List<Role> roles = roleService.list();
        return JsonUtil.toJson(new ResultVO(roles));
    }

    @ApiOperation(value = "删除角色", notes = "jiacheng yang.")
    @PostMapping(value = "/removeRoleById", produces = "application/json")
    @ResponseBody
    public String removeRoleById(@ApiParam(value = "id")@RequestParam Integer id){
        rolePermissionRelateService.removeCachePermissionInRole(Collections.singletonList(id));
        roleService.removeById(id);
        rolePermissionRelateService.remove(new LambdaUpdateWrapper<RolePermissionRelate>().eq(RolePermissionRelate::getRoleId,id));
        adminRoleRelateService.remove(new LambdaUpdateWrapper<AdminRoleRelate>().eq(AdminRoleRelate::getRoleId,id));
        return JsonUtil.toJson(new ResultVO());
    }

    @ApiOperation(value = "获取角色根据id", notes = "jiacheng yang.")
    @PostMapping(value = "/getRoleById", produces = "application/json")
    @ResponseBody
    public String getRoleById(@ApiParam(value = "id")@RequestParam Integer id){
        Role role = roleService.getById(id);
        //角色权限
        List<Permission> rolePermission=permissionService.getRolePermission(id);
        //所有权限
        List<Permission> permissions=permissionService.list();
        Map<String ,Object> map=new HashMap<>(3);
        map.put("role",role);
        map.put("allPermission",permissions);
        map.put("rolePermission",rolePermission);
        return JsonUtil.toJson(new ResultVO(map));
    }

    @ApiOperation(value = "新增或修改角色", notes = "jiacheng yang.")
    @PostMapping(value = "/addOrUpdateRole", produces = "application/json")
    @ResponseBody
    public String addOrUpdateRole(@ApiParam(value = "role")@RequestParam(value = "roleJson") String roleJson,
                                  @ApiParam(value = "权限id集合")@RequestParam(value = "idsJson") String idsJson){
        Role role = JsonUtil.getTypeObj(roleJson, Role.class);
        List<Integer> ids = JsonUtil.getListType(idsJson, Integer.class);
        if (role == null || CollUtil.isEmpty(ids)){
            return JsonUtil.toJson(new ResultVO(-1,"数据有误！"));
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
        return JsonUtil.toJson(new ResultVO(role.getId()));
    }

    @ApiOperation(value = "权限列表", notes = "jiacheng yang.")
    @PostMapping(value = "/getPermissionList", produces = "application/json")
    @ResponseBody
    public String getPermissionList(@ApiParam(value = "当前页")@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                    @ApiParam(value = "每页数量")@RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit,
                                    @ApiParam(value = "权限代码")@RequestParam(value = "urlCode",required = false) String urlCode){
        Page<Permission> pg = permissionService.getPermissionList(urlCode, page, limit);
        Map<String ,Object> map = new HashMap<>(4);
        map.put("code",0);
        map.put("msg","调用成功");
        map.put("count",pg.getTotal());
        map.put("data",pg.getRecords());
        return JsonUtil.toJson(map);
    }

    @ApiOperation(value = "根据id获取权限", notes = "jiacheng yang.")
    @PostMapping(value = "/getPermissionById", produces = "application/json")
    @ResponseBody
    public String getPermissionById(@ApiParam(value = "id")@RequestParam Integer id){
        Permission permission = permissionService.getById(id);
        return JsonUtil.toJson(new ResultVO(permission));
    }

    @ApiOperation(value = "更新权限", notes = "jiacheng yang.")
    @PostMapping(value = "/updatePermission", produces = "application/json")
    @ResponseBody
    public String updatePermission(@ApiParam(value = "权限,json")@RequestParam(value = "permissionJson") String permissionJson){
        Permission permission = JsonUtil.getTypeObj(permissionJson, Permission.class);
        if (permission == null){
            return JsonUtil.toJson(new ResultVO(-1,"数据有误！"));
        }
        if (permission.getId()!=null){
            permission.setUpdateTime(new Date());
        }
        permissionService.saveOrUpdate(permission);

        return JsonUtil.toJson(new ResultVO());
    }

    
}
