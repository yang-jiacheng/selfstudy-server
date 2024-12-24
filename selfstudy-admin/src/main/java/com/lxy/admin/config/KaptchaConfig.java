package com.lxy.admin.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha getDefaultKaptcha(){
        DefaultKaptcha captchaProducer = new DefaultKaptcha();
        Properties props = new Properties();
        //取消图片边框
        props.put("kaptcha.border","no");
        //字体黑色
        props.put("kaptcha.textproducer.font.color","black");
        //宽度
        props.put("kaptcha.image.width","110");
        //高度
        props.put("kaptcha.image.height","39");
        //文本集合，验证码值从此集合中获取
        props.put("kaptcha.textproducer.char.string","abkvptnmfGACDTyVNM1234679");
        //验证码长度
        props.put("kaptcha.textproducer.char.length","4");
        //图片样式：水纹
        props.put("kaptcha.obscurificator.impl","com.google.code.kaptcha.impl.ShadowGimpy");
        //文字间隔
        props.put("kaptcha.textproducer.char.space","3");
        //干扰实现类 有 com.google.code.kaptcha.impl.DefaultNoise 无com.google.code.kaptcha.impl.NoNoise
        props.put("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");
        props.put("kaptcha.textproducer.font.size","30");
        Config config = new Config(props);
        captchaProducer.setConfig(config);
        return captchaProducer;
    }
}
