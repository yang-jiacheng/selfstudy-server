package com.lxy.framework.security.util;

import com.lxy.framework.security.domain.StatelessUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取用户id工具类
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2023/02/13 12:27
 */
public class UserIdUtil {

    public static long getUserId() {
        long userId = -1L;
        // 获取SecurityContextHolder中的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return userId;
        }
        if (authentication instanceof UsernamePasswordAuthenticationToken authenticationToken) {
            StatelessUser principal = (StatelessUser)authenticationToken.getPrincipal();
            userId = principal.getUserId();

        }
        return userId;
    }

}
