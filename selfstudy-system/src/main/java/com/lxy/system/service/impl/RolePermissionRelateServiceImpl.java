package com.lxy.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.system.mapper.RolePermissionRelateMapper;
import com.lxy.system.po.RolePermissionRelate;
import com.lxy.system.service.RolePermissionRelateService;
import com.lxy.system.service.redis.RedisService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
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
public class RolePermissionRelateServiceImpl extends ServiceImpl<RolePermissionRelateMapper, RolePermissionRelate>
    implements RolePermissionRelateService {

    @Resource
    private RolePermissionRelateMapper rolePermissionRelateMapper;

    @Resource
    private RedisService redisService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<RolePermissionRelate> getPermissionByRole(Long roleId) {
        return rolePermissionRelateMapper.getPermissionByRole(roleId);
    }

    @Override
    public void removeCachePermissionInRole(List<Long> roleIds) {
        Set<Long> adminIds = rolePermissionRelateMapper.getAdminIdsByRoles(roleIds);
        if (CollUtil.isEmpty(adminIds)) {
            return;
        }
        List<String> keys = adminIds.stream().map(RedisKeyConstant::getAdminInfo).collect(Collectors.toList());

        redisService.deleteKeys(keys);

    }
}
