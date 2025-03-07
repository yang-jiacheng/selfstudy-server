package com.lxy.common.util;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxy.common.constant.CommonConstant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Json Web Token的工具类
 */
public class JsonWebTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonWebTokenUtil.class);
    /**
     * JWT的分隔符
     */
    public static final char SEPARATOR_CHAR = '.';
    /**
     * 密钥池
     */
    public static final String[] SECRET_KEY_POOL = {"CGls309Gb1rP7WGQ", "cX0EgfdjkFBK0Kwh", "E8xJtz46xIZDuonV"};
    /**
     * JWT过期天数
     */
    public static final int EXPIRED_DAYS = 7;

    /**
     * 产生JWT字符串
     * @param userId 用户id
     * @param device 设备类型 android、ios、web
     * @param userType 用户类型 学生2，管理员1
     * @return JWT字符串
     */
    public static String getJwtStr(Integer userId, String device,String userType) {
        if (userId == null) {
            logger.error("userId is null");
            return null;
        }
        //获取密钥
        String secretKey = getSecretKey(userId);
        //过期时间
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.DATE, EXPIRED_DAYS);
//        Date expiredTime = calendar.getTime();
        //自定义载荷属性
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(CommonConstant.PARAM_NAME_DEVICE, device);
        claimsMap.put(CommonConstant.PARAM_NAME_USER_ID, userId);
        claimsMap.put(CommonConstant.PARAM_NAME_USER_TYPE, userType);
        //产生JWT
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setClaims(claimsMap)
                // .setId(UUID) //唯一id
                .setSubject("jiacheng yang.")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //.setExpiration(expiredTime)
                .signWith(SignatureAlgorithm.HS512, secretKey);
        return jwtBuilder.compact();
    }

    /**
     * 解码JWT并校验，然后从payload中获取用户自定义数据，如果校验失败返回空
     * @param accessToken 令牌
     * @return  Claims
     */
    public static Claims getClaimsSign(String accessToken) {
        if (accessToken == null) {
            return null;
        }
        // 对JWT进行解码后获得有效载荷
        String payload = getPayload(accessToken);
        logger.debug("payload: " + payload);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> claimsUnSign = null;
        try {
            claimsUnSign = objectMapper.readValue(payload, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        int userId = 0;
        Object value = claimsUnSign.get(CommonConstant.PARAM_NAME_USER_ID);
        if (value != null) {
            if (value instanceof Integer) {
                userId = (int) value;
            } else if (value instanceof String) {
                userId = Integer.parseInt((String) value);
            }
        }

        // 获取用户的密钥
        String secretKey = getSecretKey(userId);
        if (secretKey == null || secretKey.length() < 1) {
            logger.error(userId + "用户无法得到加密密匙");
            return null;
        }

        Claims claimsSign = null;
        try {
            claimsSign = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody();
        } catch (Exception e) {
            return null;
        }
        return claimsSign;

    }


    /**
     * 读取载荷属性
     */
    public static String getPayload(String accessToken) {
        if (StrUtil.isEmpty(accessToken)) {
            return null;
        }
        String base64UrlEncodedHeader = null; // 头部
        String base64UrlEncodedPayload = null; // 有效载荷
        int delimiterCount = 0;
        byte[] decode = null;
        try {
            StringBuilder sb = new StringBuilder(128);
            for (char c : accessToken.toCharArray()) {
                if (c == SEPARATOR_CHAR) {
                    CharSequence tokenSeq = Strings.clean(sb);
                    String token = tokenSeq != null ? tokenSeq.toString() : null;
                    if (delimiterCount == 0) {
                        base64UrlEncodedHeader = token;
                    } else if (delimiterCount == 1) {
                        base64UrlEncodedPayload = token;
                    }
                    delimiterCount++;
                    sb.setLength(0);
                } else {
                    sb.append(c);
                }
            }
            // base64解码，获取payload
            decode = Base64.getUrlDecoder().decode(base64UrlEncodedPayload);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return new String(decode);
    }

    /**
     * 根据userId获取密钥
     */
    public static String getSecretKey(Integer userId) {
        int index = userId % SECRET_KEY_POOL.length;
        String baseKey = SECRET_KEY_POOL[index];
        String base64SecretKey;
        base64SecretKey = Base64.getEncoder().encodeToString(baseKey.getBytes(StandardCharsets.UTF_8));
        return base64SecretKey;
    }


    /**
     * 获取access_token
     */
    public static String getAccessToken(HttpServletRequest request,String name) {
        if (request == null) {
            return null;
        }
        String accessToken = request.getParameter(name);
        if (StrUtil.isNotEmpty(accessToken)) {
            return accessToken;
        }
        accessToken = request.getHeader(name);
        if (StrUtil.isNotEmpty(accessToken)) {
            return accessToken;
        }
        Cookie[] cookies = request.getCookies();
        if (null == cookies || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                accessToken = cookie.getValue();
                break;
            }
        }
        return accessToken;
    }

    public static Object getInfo(String key, String accessToken) {
        String payload = getPayload(accessToken);
        ObjectMapper objectMapper_JWT = new ObjectMapper();
        Map map = null;
        try {
            map = objectMapper_JWT.readValue(payload, LinkedHashMap.class);
            return map.get(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.DATE, EXPIRED_DAYS);
//        Date expiredTime = calendar.getTime();
//        System.out.println(expiredTime);
        Claims claimsSign = getClaimsSign("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4aXV5dSBMaS4iLCJ1c2VyVHlwZSI6IjIiLCJkZXZpY2UiOiJhbmRyb2lkIiwidXNlcklkIjoxLCJpYXQiOjE2NjY0MzYxMTF9.CwsG9fQ7olIQHFITiNMMG0rJgVE3z5ImXjUBUqigwQz5hxCL13684HeLRx1GEzqE97SV00Xj-zgIG347vl3tJg");
        System.out.println(claimsSign);
        claimsSign.get("");
//        System.out.println(getJwtStr(1,"ios","2"));
        //eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4aXV5dSBMaS4iLCJ1c2VyVHlwZSI6IjIiLCJkZXZpY2UiOiJpb3MiLCJ1c2VySWQiOjEsImlhdCI6MTY2NjQzMzg2OH0.ifJYZX2H1gV2H2a0CTgXB3yE7jK-5B1_fBJaMzOQ-9wntYr3MhMVsdJ0d9YZiLLoC8lrr7FDiX01MmV283AmQA
    }

}
