package com.lxy.common.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2023/01/03 14:17
 * @version 1.0
 */

@Slf4j
public class WebUtil {

    public static void renderString(HttpServletResponse response,String str){
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            response.getWriter().write(str);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void renderRedirect(HttpServletResponse response,String url){
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            response.sendRedirect(ImgConfigUtil.getAccessUrl()+url);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
