package com.lxy.app.security.util;

import com.lxy.common.security.bo.StatelessUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/13 12:27
 * @Version: 1.0
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
