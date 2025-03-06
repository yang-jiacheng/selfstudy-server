package com.lxy.app.security.util;

import com.lxy.common.security.bo.StatelessUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

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
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (!Objects.isNull(authentication)){
            StatelessUser principal = (StatelessUser)authentication.getPrincipal();
            userId = principal.getUserInfo().getId();
        }
        return userId;
    }

}
