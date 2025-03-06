package com.lxy.app.controller;

import com.lxy.common.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.*;

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
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    @RequestMapping("/")
    public String index() {

        return "hello selfstudy-mobile-app !";
    }

    @GetMapping("/authNeed")
    @ResponseBody
    public String authNeed() {
        throw new RuntimeException("RuntimeException");
//        return "authNeed";
    }

    @GetMapping("/permitNeed")
    @ResponseBody
    public String permitNeed() {
        throw new IndexOutOfBoundsException("IndexOutOfBoundsException");

//
//
//        return "permitNeed";
    }

    @GetMapping("/helloWorld")
    public String helloWorld() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        list.get(3);
        return "helloWorld";
    }

}
