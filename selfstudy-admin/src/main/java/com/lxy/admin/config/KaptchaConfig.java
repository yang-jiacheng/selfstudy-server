package com.lxy.admin.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.KAPTCHA_BORDER;
import static com.google.code.kaptcha.Constants.KAPTCHA_IMAGE_HEIGHT;
import static com.google.code.kaptcha.Constants.KAPTCHA_IMAGE_WIDTH;
import static com.google.code.kaptcha.Constants.KAPTCHA_NOISE_COLOR;
import static com.google.code.kaptcha.Constants.KAPTCHA_NOISE_IMPL;
import static com.google.code.kaptcha.Constants.KAPTCHA_OBSCURIFICATOR_IMPL;
import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_CONFIG_KEY;
import static com.google.code.kaptcha.Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH;
import static com.google.code.kaptcha.Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE;
import static com.google.code.kaptcha.Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING;
import static com.google.code.kaptcha.Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR;
import static com.google.code.kaptcha.Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE;
import static com.google.code.kaptcha.Constants.KAPTCHA_TEXTPRODUCER_IMPL;

@Configuration
public class KaptchaConfig {
    /**
     * java.lang.RuntimeException: Fontconfig head is null, check your fonts or fonts configuration
     * 报错解决方案：
     * yum install -y fontconfig
     */

    @Bean(name = "defaultProducer")
    public DefaultKaptcha defaultKaptcha() {
        DefaultKaptcha captchaProducer = new DefaultKaptcha();
        Properties props = new Properties();
        //取消图片边框
        props.put(KAPTCHA_BORDER, "no");
        //字体黑色
        props.put(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        //宽度
        props.put(KAPTCHA_IMAGE_WIDTH, "120");
        //高度
        props.put(KAPTCHA_IMAGE_HEIGHT, "38");
        // KAPTCHA_SESSION_KEY
        props.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCode");

        //文本集合，验证码值从此集合中获取
        props.put(KAPTCHA_TEXTPRODUCER_CHAR_STRING, "abkvptnmfGACDTyVNM1234679");
        //验证码长度
        props.put(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        // 验证码噪点颜色 默认为Color.BLACK
        props.setProperty(KAPTCHA_NOISE_COLOR, "white");
        //图片样式：水纹
        props.put(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        //文字间隔
        props.put("kaptcha.textproducer.char.space", "3");
        //干扰实现类 有 com.google.code.kaptcha.impl.DefaultNoise 无com.google.code.kaptcha.impl.NoNoise
        props.put(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        // 验证码文本字符大小 默认为40
        props.put(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "30");
        Config config = new Config(props);
        captchaProducer.setConfig(config);
        return captchaProducer;
    }

    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha captchaProducerMath() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        //取消图片边框
        properties.put(KAPTCHA_BORDER, "no");
        //字体黑色
        properties.put(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        //宽度
        properties.put(KAPTCHA_IMAGE_WIDTH, "120");
        //高度
        properties.put(KAPTCHA_IMAGE_HEIGHT, "38");
        // KAPTCHA_SESSION_KEY
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");
        // 验证码文本生成器
        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, "com.lxy.admin.config.KaptchaMathCreator");

        // 验证码文本字符长度 默认为5
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");
        // 验证码噪点颜色 默认为Color.BLACK
        properties.setProperty(KAPTCHA_NOISE_COLOR, "white");
        // 验证码文本字符间距 默认为2
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");
        // 干扰实现类
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        // 图片样式 水纹com.google.code.kaptcha.impl.WaterRipple 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy 阴影com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        // 验证码文本字符大小 默认为40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "30");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

}
