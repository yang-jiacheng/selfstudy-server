package com.lxy.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.lxy.common.domain.StatelessAdmin;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.redis.util.RedisKeyUtil;
import com.lxy.common.service.RolePermissionRelateService;
import com.lxy.common.po.RolePermissionRelate;
import com.lxy.common.mapper.RolePermissionRelateMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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


    @Autowired
    private RolePermissionRelateMapper rolePermissionRelateMapper;

    @Autowired
    private CommonRedisService commonRedisService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

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
                .map(RedisKeyUtil::getAdminLoginStatus)
                .collect(Collectors.toList());

        List<String > values = redisTemplate.opsForValue().multiGet(keys);
        if (CollUtil.isEmpty(values)){
            return;
        }
        Map<String,String> map = new HashMap<>(values.size());
        for (String value : values) {
            List<StatelessAdmin> loginList = JSONObject.parseArray(value, StatelessAdmin.class);
            if (CollUtil.isNotEmpty(loginList)){
                loginList.forEach(lis -> lis.setPermissions(null));
                Integer id = loginList.get(0).getAdminInfo().getId();
                map.put(RedisKeyUtil.getAdminLoginStatus(id), JsonUtil.toJson(loginList));
            }
        }

        commonRedisService.insertBatchString(map,-1);

    }
}
