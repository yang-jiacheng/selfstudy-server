package com.lxy.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.common.po.Permission;
import com.lxy.common.mapper.PermissionMapper;
import com.lxy.common.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private final PermissionMapper permissionMapper;

    @Autowired
    public PermissionServiceImpl(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @Override
    public List<Permission> getRolePermission(Integer roleId) {
        return permissionMapper.getRolePermission(roleId);
    }

    @Override
    public Page<Permission> getPermissionList(String urlCode, Integer page, Integer limit) {
        Page<Permission> pg =new Page<>(page,limit);
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(urlCode)){
            wrapper.like(Permission::getUrl,urlCode);
        }
        pg = this.page(pg,wrapper);
        return pg;
    }

}
