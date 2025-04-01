package com.lxy.framework.security.encoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @Description: 前端传过来的就是加密后的，无需再编码
 * @author: jiacheng yang.
 * @Date: 2023/02/22 17:46
 * @Version: 1.0
 */

@Component
public class MinePasswordEncoder implements PasswordEncoder {

    public final static String sha256 = "{sha256}";

    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        String sequenceStr = charSequence.toString();
        if (s.startsWith(sha256)) {
            s = s.substring(sha256.length());
        }
        return sequenceStr.equals(s);
    }



}
