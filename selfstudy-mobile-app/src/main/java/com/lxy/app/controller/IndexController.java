package com.lxy.app.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.lxy.app.security.util.UserIdUtil;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.po.Feedback;
import com.lxy.common.security.bo.StatelessAdmin;
import com.lxy.common.service.RedisService;
import com.lxy.common.service.UserService;
import com.lxy.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/20 11:15
 * @Version: 1.0
 */

@RestController
public class IndexController {

    private final static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private RedisService redisService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    @RequestMapping("/")
    public String index() {

        return "hello selfstudy-mobile-app !";
    }

}
