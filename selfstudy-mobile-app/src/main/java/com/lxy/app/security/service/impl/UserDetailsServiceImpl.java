package com.lxy.app.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.system.security.bo.StatelessUser;
import com.lxy.system.po.User;
import com.lxy.system.security.encoder.MinePasswordEncoder;
import com.lxy.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/22 17:55
 * @Version: 1.0
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,username);
        User user = userService.getOne(wrapper);

        if (Objects.isNull(user)){
            throw new RuntimeException("Wrong username ！");
        }
        //不用他的编码器 密码就必须加个前缀，新版的spring security要求，难崩
        String password = MinePasswordEncoder.sha256 + user.getPassword();
        //封装成StatelessUser对象返回
        return new StatelessUser(user.getId(),password,user.getPhone());
    }
}
