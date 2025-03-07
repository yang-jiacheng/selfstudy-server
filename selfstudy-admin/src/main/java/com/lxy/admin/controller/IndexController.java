package com.lxy.admin.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.lxy.admin.security.util.AdminIdUtil;
import com.lxy.common.bo.R;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.po.Feedback;
import com.lxy.common.po.User;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.service.RedisService;
import com.lxy.common.util.JsonUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/10/08 18:50
 * @Version: 1.0
 */

@Controller
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;


    @Resource
    private RedisService redisService;

    @RequestMapping("/")
    @ResponseBody
    public String index() {

        return "hello selfstudy-admin !";
    }

    @GetMapping("/permitNeed")
    @ResponseBody
    public String permitNeed() {
        Feedback feedback = new Feedback();
        feedback.setId(123);
        feedback.setContent("zfdsafdsafdsa你好啊");
        feedback.setPic("测试");
        feedback.setCreateTime(new Date());


        List<Feedback> feedbackList = new ArrayList<>();
        feedbackList.add(feedback);

        redisService.setObject("feedback", feedback, 120L, TimeUnit.SECONDS);
        redisService.setObject("feedbackList", JsonUtil.toJson(feedbackList), -1L, TimeUnit.SECONDS);
        redisService.setList("feedbackList222", feedbackList);
        redisService.setHashValue("feedbackHash", "测试1", feedback);

        redisService.setObject("feedback33", feedbackList, 120L, TimeUnit.SECONDS);

        Feedback feedback1 = redisService.getObject("feedback", Feedback.class);
        List<Feedback> feedbackList2 = redisService.getList("feedbackList222");
        List<Feedback> feedbackList1 = JsonUtil.getListType(redisService.getObject("feedbackList", String.class), Feedback.class);
        Feedback hashValue = redisService.getHashValue("feedbackHash", "测试1", Feedback.class);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("测试1");
        List<Feedback> hashValueBatch = redisService.getHashValueBatch("feedbackHash", strings, Feedback.class);

        List<Feedback> feedback33 = redisService.getObject("feedback33", ArrayList.class);
        throw new RuntimeException("permitNeed");
    }

    @RequestMapping("/login")
    public String login() {
        return "login2";
    }

    @RequestMapping("/error403")
    public String error403() {
        return "error403two";
    }

    @RequestMapping("/404")
    public String error404() {
        return "error/404";
    }

    @PostMapping("/Kaptcha")
    @ResponseBody
    public R<Map<String, Object>> getKaptchaImage()  {
        Map<String, Object> map = new HashMap<>(2);
        String uuid = IdUtil.simpleUUID();
        String verifyKey = RedisKeyConstant.getMathCodeKey(uuid);

        String capText = captchaProducerMath.createText();
        String capStr = capText.substring(0, capText.lastIndexOf("@"));
        String code = capText.substring(capText.lastIndexOf("@") + 1);
        BufferedImage image = captchaProducerMath.createImage(capStr);
        //验证码存到redis
        redisService.setObject(verifyKey, code, 120L, TimeUnit.SECONDS);
        // 转换流信息写出
        try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", os);
            map.put("uuid", uuid);
            map.put("img", Base64.encode(os.toByteArray()));
            return R.ok(map);
        } catch (IOException e) {
            return R.fail("验证码生成失败");
        }

    }

}
