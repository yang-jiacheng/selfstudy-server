package com.lxy.admin.util;

import com.google.code.kaptcha.Constants;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @Description: 验证码的工具类
 * @author: jiacheng yang.
 * @Date: 2021/5/19 20:29
 * @Version: 1.0
 */

@Component
public class CodeUtil {

    private final static String PASS = "PASS";

    public boolean checkVerifyCode(String verifyCodeActual,HttpServletRequest request){

        String value = (String)request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (value==null){
            return false;
        }
        value = value.toUpperCase();
        //为了防止用户输入正确的验证码,但却因为大小写的原因失败,所以统一转换成大写字母
        verifyCodeActual = verifyCodeActual.toUpperCase();
        if (PASS.equals(verifyCodeActual)){
            return true;
        }
        return value.equals(verifyCodeActual);
    }
}
