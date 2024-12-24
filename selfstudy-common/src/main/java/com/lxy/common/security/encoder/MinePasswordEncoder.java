package com.lxy.common.security.encoder;

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
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        String sequenceStr = charSequence.toString();
        return sequenceStr.equals(s);
    }
}
