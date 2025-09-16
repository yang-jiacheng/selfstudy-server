package com.lxy.admin.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.google.code.kaptcha.Producer;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.domain.R;
import com.lxy.common.util.SmsUtil;
import com.lxy.common.vo.SmsSendVO;
import com.lxy.system.service.PermissionService;
import com.lxy.system.service.RedisService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * index
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/10/08 18:50
 */

@Slf4j
@Controller
public class IndexController {

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;
    @Resource
    private RedisService redisService;
    @Resource
    private PermissionService permissionService;

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "hello selfstudy-admin !";
    }

    @RequestMapping("/hello")
    @ResponseBody
    public void hello() {


    }

    @RequestMapping("/world")
    @ResponseBody
    public void world() {
        String phoneNumbers = "15607150562";
        SmsSendVO smsDTO = new SmsSendVO();
        smsDTO.setTemplateCode(SmsUtil.TEMPLATE_CODE);
        List<SmsSendVO.TemplateParam> params = new ArrayList<>();
        params.add(new SmsSendVO.TemplateParam("code", SmsUtil.getRandomCode()));
        smsDTO.setTemplateParams(params);
        boolean b = SmsUtil.sendMessage(phoneNumbers, smsDTO);
        log.info("短信发送结果：{}", b);
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
     *
     * @author jiacheng yang.
     * @since 2025/03/07 15:14
     */
    @PostMapping("/Kaptcha")
    @ResponseBody
    public R<Map<String, Object>> getKaptchaImage() {
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
