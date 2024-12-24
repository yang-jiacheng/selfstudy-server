package com.lxy.admin.security.util;

import com.lxy.common.domain.StatelessAdmin;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (!Objects.isNull(authentication)){
            StatelessAdmin principal = (StatelessAdmin)authentication.getPrincipal();
            userId = principal.getAdminInfo().getId();
        }
        return userId;
    }

}
