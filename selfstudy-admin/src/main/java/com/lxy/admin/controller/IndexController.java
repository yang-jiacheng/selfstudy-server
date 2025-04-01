package com.lxy.admin.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.google.code.kaptcha.Producer;
import com.lxy.common.domain.R;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.system.service.RedisService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
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

    /**
     * 验证码生成
     * @author jiacheng yang.
     * @since 2025/03/07 15:14
     */
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
