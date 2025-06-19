package com.lxy.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.system.dto.AdminInfoPageDTO;
import com.lxy.system.mapper.AdminInfoMapper;
import com.lxy.system.po.AdminInfo;
import com.lxy.system.service.AdminInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.system.service.RedisService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
@Service
public class AdminInfoServiceImpl extends ServiceImpl<AdminInfoMapper, AdminInfo> implements AdminInfoService {

    @Resource
    private AdminInfoMapper adminInfoMapper;
    @Resource
    private RedisService redisService;

    @Override
    public List<String> getPermissionsById(Integer userId) {
        return adminInfoMapper.getPermissionsById(userId);
    }

    @Override
    public AdminInfo getAdminInfoByUsernameAndPassword(String username, String password) {
        LambdaQueryWrapper<AdminInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminInfo::getStatus,1).eq(AdminInfo::getUsername,username).eq(AdminInfo::getPassword, password);
        return this.getOne(wrapper);
    }


    @Override
    public Page<AdminInfo> getAdminInfoPageList(AdminInfoPageDTO pageDTO) {
        Page<AdminInfo> pg = new Page<AdminInfo>(pageDTO.getPage(),pageDTO.getLimit());
        LambdaQueryWrapper<AdminInfo> wrapper = new LambdaQueryWrapper<AdminInfo>();
        wrapper.orderByDesc(AdminInfo::getId);
        wrapper.ne(AdminInfo::getId,pageDTO.getUserId());
        String name = pageDTO.getName();
        if (StrUtil.isNotEmpty(name)){
            wrapper.like(AdminInfo::getName,name);
        }
        pg = this.page(pg,wrapper);
        return pg;
    }

    @Override
    public void removeCachePermissionInAdminIds(List<Integer> adminIds) {
        if (CollUtil.isEmpty(adminIds)){
            return;
        }
        List<String> keys = adminIds.stream()
                .map(RedisKeyConstant::getAdminLoginStatus)
                .collect(Collectors.toList());

        redisService.deleteKeys(keys);
    }
}
