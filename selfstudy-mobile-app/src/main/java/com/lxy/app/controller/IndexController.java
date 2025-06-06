package com.lxy.app.controller;

import com.lxy.system.service.RedisService;
import com.lxy.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/12/20 11:15
 * @version 1.0
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

    @RequestMapping("/hello")
    @ResponseBody
    public void hello() {
        throw new RuntimeException("hello error");
    }

    @RequestMapping("/world")
    @ResponseBody
    public void world() {
        throw new RuntimeException("world error");
    }

    @GetMapping("/test")
    @ResponseBody
    public void test() {
        userService.test();
    }

}
