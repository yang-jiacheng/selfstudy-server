package com.lxy.admin.security.util;

import com.lxy.common.domain.StatelessAdmin;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/13 12:27
 * @Version: 1.0
 */
public class AdminIdUtil {

    public static int getAdminId(){
        int userId = -1;
        //获取SecurityContextHolder中的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ) {
            return userId;
        }
        if (authentication instanceof UsernamePasswordAuthenticationToken authenticationToken) {
            StatelessAdmin principal = (StatelessAdmin)authenticationToken.getPrincipal();
            userId = principal.getAdminInfo().getId();

        }
        return userId;
    }

}
