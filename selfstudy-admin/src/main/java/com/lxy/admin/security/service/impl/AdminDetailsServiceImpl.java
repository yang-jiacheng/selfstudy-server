package com.lxy.admin.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.common.domain.StatelessAdmin;
import com.lxy.common.po.AdminInfo;
import com.lxy.common.security.encoder.MinePasswordEncoder;
import com.lxy.common.service.AdminInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/23 21:08
 * @Version: 1.0
 */

@Service
public class AdminDetailsServiceImpl implements UserDetailsService {

    private final AdminInfoService adminInfoService;

    @Autowired
    public AdminDetailsServiceImpl(AdminInfoService adminInfoService) {
        this.adminInfoService = adminInfoService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<AdminInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminInfo::getUsername,username);
        AdminInfo adminInfo = adminInfoService.getOne(wrapper);
        if (Objects.isNull(adminInfo)){
            throw new RuntimeException("Wrong username ！");
        }
        //根据用户查询权限信息 添加到StatelessAdmin中
        List<String> permissions = adminInfoService.getPermissionsById(adminInfo.getId());
        //封装成StatelessAdmin对象返回
        //密码必须加个前缀，新版的spring security要求，难崩
        adminInfo.setPassword(MinePasswordEncoder.sha256 + adminInfo.getPassword());
        StatelessAdmin statelessAdmin = new StatelessAdmin(adminInfo);
        statelessAdmin.setPermissions(permissions);
        return statelessAdmin;
    }
}
