package com.lxy.admin.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.lxy.admin.security.util.AdminIdUtil;
import com.lxy.common.po.Feedback;
import com.lxy.common.po.User;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.service.RedisService;
import com.lxy.common.util.JsonUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Resource
    private Producer captchaProducer;
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

    @GetMapping("/Kaptcha")
    public void getKaptchaImage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        resp.setHeader("Pragma", "no-cache");
        // return a jpeg
        resp.setContentType("image/jpeg");
        // create the text for the image
        String capText = captchaProducer.createText();
        HttpSession session = req.getSession();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY,capText);
        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = resp.getOutputStream();
        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

}
