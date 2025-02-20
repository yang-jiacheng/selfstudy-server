package com.lxy.admin.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.lxy.admin.util.WordUtil;
import com.lxy.common.domain.R;
import com.lxy.common.util.CommonUtil;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/10/08 18:50
 * @Version: 1.0
 */

@Controller
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Producer captchaProducer;

    @Autowired
    public IndexController(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "hello selfstudy-admin !";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
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
