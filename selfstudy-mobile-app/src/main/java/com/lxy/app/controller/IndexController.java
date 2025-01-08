package com.lxy.app.controller;

import cn.hutool.core.util.StrUtil;
import com.lxy.common.domain.R;
import com.lxy.common.po.Role;
import com.lxy.common.util.SmsUtil;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/sendSMS")
    public R<Object> sendSMS(@RequestParam("phone") String phone) {
        String code = SmsUtil.getRandomCode();
        boolean flag = SmsUtil.sendMessage(phone, code);
        return R.ok(flag);
    }

}
