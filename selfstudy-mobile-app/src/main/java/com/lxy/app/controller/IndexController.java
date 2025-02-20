package com.lxy.app.controller;

import cn.hutool.core.util.StrUtil;
import com.lxy.common.domain.R;
import com.lxy.common.po.Role;
import com.lxy.common.po.User;
import com.lxy.common.service.UserService;
import com.lxy.common.util.SmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
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

//    @GetMapping("/authNeed")
//    public String authNeed() {
//        List<Integer> list = Arrays.asList(1, 2, 3);
//        list.get(3);
//        return "authNeed";
//    }
//
//    @GetMapping("/permitNeed")
//    public String permitNeed() {
//        List<Integer> list = Arrays.asList(1, 2, 3);
//        list.get(3);
//        return "permitNeed";
//    }
//
//    @GetMapping("/helloWorld")
//    public String helloWorld() {
//        List<Integer> list = Arrays.asList(1, 2, 3);
//        list.get(3);
//        return "helloWorld";
//    }

}
