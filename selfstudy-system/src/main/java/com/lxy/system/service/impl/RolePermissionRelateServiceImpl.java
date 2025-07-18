package com.lxy.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.system.service.RedisService;
import com.lxy.system.service.RolePermissionRelateService;
import com.lxy.system.po.RolePermissionRelate;
import com.lxy.system.mapper.RolePermissionRelateMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色权限关联表 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
@Service
public class RolePermissionRelateServiceImpl extends ServiceImpl<RolePermissionRelateMapper, RolePermissionRelate> implements RolePermissionRelateService {


    @Resource
    private RolePermissionRelateMapper rolePermissionRelateMapper;

    @Resource
    private RedisService redisService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public List<RolePermissionRelate> getPermissionByRole(Integer roleId) {
        return rolePermissionRelateMapper.getPermissionByRole(roleId);
    }

    @Override
    public void removeCachePermissionInRole(List<Integer> roleIds) {
        Set<Integer> adminIds = rolePermissionRelateMapper.getAdminIdsByRoles(roleIds);
        if (CollUtil.isEmpty(adminIds)){
            return;
        }
        List<String> keys = adminIds.stream()
                .map(RedisKeyConstant::getAdminInfo)
                .collect(Collectors.toList());

        redisService.deleteKeys(keys);

    }
}
