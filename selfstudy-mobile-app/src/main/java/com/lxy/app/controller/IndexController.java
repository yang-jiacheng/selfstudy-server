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

    @GetMapping("/authNeed")
    @ResponseBody
    public String authNeed() {
        int userId = UserIdUtil.getUserId();
        Feedback feedback = new Feedback();
        feedback.setId(123);
        feedback.setContent("zfdsafdsafdsa你好啊");
        feedback.setPic("测试");
        feedback.setCreateTime(new Date());


        List<Feedback> feedbackList = new ArrayList<>();
        feedbackList.add(feedback);


        Map<String, List<Feedback>> map = new HashMap<>();
        map.put("Feedback", feedbackList);
        map.put("Feedback2", feedbackList);
        redisService.setObjectBatch(map, 10000L, TimeUnit.SECONDS);

        List<String> keys = new ArrayList<>();
        keys.add("Feedback");
        keys.add("Feedback2");

        List<Feedback> feedback1 = redisService.getObject("Feedback", List.class);
        List<Object> values = redisTemplate.opsForValue().multiGet(keys);
        Map<String,Object> map222 = new HashMap<>(values.size());
        for (Object value : values) {
            List<Feedback> loginList = (List<Feedback>) value.getClass().cast(value);
            if (CollUtil.isNotEmpty(loginList)){
                map.put(RandomUtil.randomString(5), loginList);
            }
        }
        throw new IndexOutOfBoundsException("authNeed");
    }

    @GetMapping("/permitNeed")
    @ResponseBody
    public String permitNeed() {
        int userId = UserIdUtil.getUserId();
        throw new IndexOutOfBoundsException("permitNeed");

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
