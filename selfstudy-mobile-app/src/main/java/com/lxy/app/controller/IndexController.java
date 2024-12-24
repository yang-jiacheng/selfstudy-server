package com.lxy.app.controller;

import cn.hutool.core.util.StrUtil;
import com.lxy.common.po.Role;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/20 11:15
 * @Version: 1.0
 */

@RestController
public class IndexController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping("/")
    public String index() {
        return "hello selfstudy-mobile-app !";
    }



}
