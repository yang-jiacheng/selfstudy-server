package com.lxy.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.security.bo.StatelessUser;
import com.lxy.common.service.RedisService;
import com.lxy.common.service.RolePermissionRelateService;
import com.lxy.common.po.RolePermissionRelate;
import com.lxy.common.mapper.RolePermissionRelateMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
                .map(RedisKeyConstant::getAdminLoginStatus)
                .collect(Collectors.toList());

        List<Object> values = redisTemplate.opsForValue().multiGet(keys);
        if (values == null || values.stream().allMatch(Objects::isNull)){
            return;
        }
        Map<String,List<StatelessUser>> map = new HashMap<>(values.size());
        for (Object value : values) {
            List<StatelessUser> loginList = (List<StatelessUser>)value.getClass().cast(value);
            if (CollUtil.isNotEmpty(loginList)){
                loginList.forEach(lis -> lis.setPermissions(new ArrayList<>()));
                Integer id = loginList.get(0).getUserId();
                map.put(RedisKeyConstant.getAdminLoginStatus(id), loginList);
            }
        }

        redisService.setObjectBatch(map, -1L, null);

    }
}
