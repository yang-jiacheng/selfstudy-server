package com.lxy.admin.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxy.common.domain.R;
import com.lxy.common.util.JsonUtil;
import com.lxy.system.dto.AdminEditDTO;
import com.lxy.system.dto.AdminStatusDTO;
import com.lxy.system.dto.RoleEditDTO;
import com.lxy.system.po.*;
import com.lxy.system.service.*;
import com.lxy.system.vo.PermissionTreeVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
     * @author jiacheng yang.
     * @since 2025/6/13 10:48
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeRoleById(Integer id) {
        rolePermissionRelateService.removeCachePermissionInRole(Collections.singletonList(id));
        roleService.removeById(id);
        rolePermissionRelateService.remove(new LambdaUpdateWrapper<RolePermissionRelate>().eq(RolePermissionRelate::getRoleId,id));
        adminRoleRelateService.remove(new LambdaUpdateWrapper<AdminRoleRelate>().eq(AdminRoleRelate::getRoleId,id));
    }


    /**
     * 获取角色和权限
     * @author jiacheng yang.
     * @since 2025/6/13 10:50
     */
    public Map<String ,Object> getRoleDetail(Integer id) {
        Role role = roleService.getById(id);
        //角色权限
        List<Permission> rolePermission=permissionService.getRolePermission(id);
        List<Integer> ids = rolePermission.stream().map(Permission::getId).toList();
        //所有权限
        List<PermissionTreeVO> permissions=permissionService.getPermissionTree();
        Map<String ,Object> map=new HashMap<>(3);
        map.put("role",role);
        map.put("allPermission",permissions);
        map.put("rolePermission", ids);
        return map;
    }

    /**
     * 编辑 角色
     * @author jiacheng yang.
     * @since 2025/6/13 10:54
     * @return 角色id
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer addOrUpdateRole(RoleEditDTO roleEditDTO){
        List<Integer> permissionIds = roleEditDTO.getPermissionIds();
        Role role = new Role();
        BeanUtil.copyProperties(roleEditDTO,role);

        //移除缓存中角色对应的用户的登录状态
        if (role.getId()!=null){
            role.setUpdateTime(new Date());
            rolePermissionRelateService.removeCachePermissionInRole(Collections.singletonList(role.getId()));
        }

        roleService.saveOrUpdate(role);

        //先删
        Integer roleId = role.getId();
        rolePermissionRelateService.remove(new LambdaUpdateWrapper<RolePermissionRelate>().eq(RolePermissionRelate::getRoleId,roleId));
        //再新增
        List<RolePermissionRelate> list = new ArrayList<>(permissionIds.size());
        RolePermissionRelate relate = null;
        for (Integer id : permissionIds) {
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
     * @author jiacheng yang.
     * @since 2025/6/17 19:25
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Object> editAdminInfo(AdminEditDTO adminEditDTO){
        AdminInfo adminInfo = adminEditDTO.getAdminInfo();
        List<Integer> roleIds = adminEditDTO.getRoleIds();
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
            return R.fail("手机号已被使用！");
        }
        adminInfoService.saveOrUpdate(adminInfo);
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
        return R.ok();
    }

    /**
     * 修改后管用户状态
     * @author jiacheng yang.
     * @since 2025/6/17 19:34
     */
    @Transactional(rollbackFor = Exception.class)
    public void disabledAdminInfo(AdminStatusDTO dto){
        LambdaUpdateWrapper<AdminInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AdminInfo::getId,dto.getId()).set(AdminInfo::getStatus,dto.getStatus());
        adminInfoService.update(wrapper);
        adminInfoService.removeCachePermissionInAdminIds(Arrays.asList(dto.getId()));
    }

    /**
     * 删除后管用户
     * @author jiacheng yang.
     * @since 2025/6/17 19:34
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeAdminInfoByIds(List<Integer> userIds){
        adminInfoService.removeByIds(userIds);
        adminRoleRelateService.remove(new LambdaQueryWrapper<AdminRoleRelate>().in(AdminRoleRelate::getAdminId,userIds));
        adminInfoService.removeCachePermissionInAdminIds(userIds);
    }

}
