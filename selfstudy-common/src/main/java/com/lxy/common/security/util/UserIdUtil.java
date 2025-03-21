package com.lxy.common.security.util;

import com.lxy.common.security.bo.StatelessUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * 获取用户id工具类
 * @author  jiacheng yang.
 * @since  2023/02/13 12:27
 * @version  1.0
 */
public class UserIdUtil {

    public static int getUserId(){
        int userId = -1;
        //获取SecurityContextHolder中的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ) {
            return userId;
        }
        if (authentication instanceof UsernamePasswordAuthenticationToken authenticationToken) {
            StatelessUser principal = (StatelessUser) authenticationToken.getPrincipal();
            userId = principal.getUserId();

        }
        return userId;
    }

}
