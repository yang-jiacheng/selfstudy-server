package com.lxy.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lxy.common.model.TokenPair;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 双Token机制的JWT工具类
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/01/18 10:00
 */
@Slf4j
public class DualTokenUtil {

    /**
     * JWT中userType参数的key名称 LogUserType
     */
    public static final String PARAM_NAME_USER_TYPE = "userType";
    /**
     * JWT中userId参数的key名称
     */
    public static final String PARAM_NAME_USER_ID = "userId";
    /**
     * JWT中jId参数的key名称
     */
    public static final String PARAM_NAME_JID = "jId";
    /**
     * token类型
     */
    public static final String PARAM_NAME_TOKEN_TYPE = "tokenType";
    /**
     * token的名称(管理员)
     */
    public static final String TOKEN_NAME_ADMIN = "selfStudyAdminToken";
    /**
     * token的名称(客户端)
     */
    public static final String TOKEN_NAME_APP = "studyRoomToken";
    /**
     * refresh token的名称
     */
    public static final String TOKEN_REFRESH = "refreshToken";
    /**
     * 密钥池
     */
    private static final String[] SECRET_KEY_POOL = {"CGls309Gb1rP7WGQ", "cX0EgfdjkFBK0Kwh", "E8xJtz46xIZDuonV"};
    /**
     * AccessToken过期时间(分钟)
     */
    private static final int ACCESS_TOKEN_EXPIRE_MINUTES = 15;
    /**
     * RefreshToken过期时间(天)
     */
    private static final int REFRESH_TOKEN_EXPIRE_DAYS = 30;
    /**
     * Token类型标识
     */
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";

    private static final String SUBJECT = "yangjiacheng";

    public static String getToken(HttpServletRequest request, String name) {
        // 优先从Header获取
        String token = request.getHeader(name);
        if (StrUtil.isNotEmpty(token)) {
            return token;
        }

        // 从参数获取
        token = request.getParameter(name);
        StrUtil.isNotEmpty(token);

        return token;
    }

    /**
     * 生成双Token
     *
     * @param userId 用户ID
     * @param userType 用户类型
     * @return TokenPair
     */
    public static TokenPair generateTokenPair(Long userId, String userType) {
        Date now = new Date();
        Date accessExpires = DateUtil.offsetMinute(now, ACCESS_TOKEN_EXPIRE_MINUTES);
        Date refreshExpires = DateUtil.offsetDay(now, REFRESH_TOKEN_EXPIRE_DAYS);

        // 生成AccessToken
        String accessId = IdUtil.simpleUUID();
        String accessToken = generateToken(accessId, userId, userType, TOKEN_TYPE_ACCESS, accessExpires);

        // 生成RefreshToken
        String refreshId = IdUtil.simpleUUID();
        String refreshToken = generateToken(refreshId, userId, userType, TOKEN_TYPE_REFRESH, refreshExpires);

        TokenPair tokenPair = new TokenPair(refreshId, accessToken, refreshToken, accessExpires, refreshExpires);
        tokenPair.setUserId(userId);
        tokenPair.setUserType(userType);

        return tokenPair;
    }

    /**
     * 生成单个Token
     */
    private static String generateToken(String jwtId, Long userId, String userType, String tokenType,
        Date expiration) {
        String secretKey = getSecretKey(userId);

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(PARAM_NAME_USER_ID, userId);
        claimsMap.put(PARAM_NAME_USER_TYPE, userType);
        claimsMap.put(PARAM_NAME_TOKEN_TYPE, tokenType);
        claimsMap.put(PARAM_NAME_JID, jwtId);

        return Jwts.builder().setClaims(claimsMap).setSubject(SUBJECT).setIssuedAt(new Date()).setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    /**
     * 验证AccessToken
     *
     * @param accessToken 访问Token
     * @return 是否有效
     */
    public static boolean validateAccessToken(String accessToken) {
        Claims claims = parseToken(accessToken);
        if (claims == null) {
            return false;
        }

        String tokenType = (String)claims.get(PARAM_NAME_TOKEN_TYPE);
        return TOKEN_TYPE_ACCESS.equals(tokenType);
    }

    /**
     * 验证RefreshToken
     *
     * @param refreshToken 刷新Token
     * @return 是否有效
     */
    public static boolean validateRefreshToken(String refreshToken) {
        Claims claims = parseToken(refreshToken);
        if (claims == null) {
            return false;
        }

        String tokenType = (String)claims.get(PARAM_NAME_TOKEN_TYPE);
        return TOKEN_TYPE_REFRESH.equals(tokenType);
    }

    /**
     * 获取载荷
     */
    private static String getPayload(String token) {
        if (StrUtil.isEmpty(token)) {
            return null;
        }

        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }

            byte[] decode = Base64.getUrlDecoder().decode(parts[1]);
            return new String(decode, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Failed to get payload", e);
            return null;
        }
    }

    /**
     * 解析Token
     *
     * @param token JWT Token
     * @return Claims
     */
    public static Claims parseToken(String token) {
        if (StrUtil.isEmpty(token)) {
            return null;
        }

        try {
            // 先解析获取用户ID
            String payload = getPayload(token);
            Map<String, Object> claimsMap = JsonUtil.getObj(payload, new TypeReference<HashMap<String, Object>>() {});
            Object userIdObj = claimsMap.get(PARAM_NAME_USER_ID);
            if (ObjectUtil.isNull(userIdObj) || !(userIdObj instanceof Number)) {
                log.error("token中未获取到userId：{}", token);
                return null;
            }
            Long userId = ((Number)userIdObj).longValue();
            // 获取密钥并解析
            String secretKey = getSecretKey(userId);
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.error("token已过期: {}", token);
            return null;
        } catch (Exception e) {
            log.error("无法解析token: {}", token, e);
            return null;
        }
    }

    /**
     * 获取Claims中的Long值
     */
    public static Long getLongFromClaims(Claims claims, String key) {
        if (ObjectUtil.isNull(claims) || StrUtil.isEmpty(key)) {
            return null;
        }
        Object value = claims.get(key);
        if (value == null) {
            return null;
        }
        try {
            if (value instanceof Number) {
                return ((Number)value).longValue();
            }

            if (value instanceof String) {
                String str = ((String)value).trim();
                if (StrUtil.isNumeric(str)) {
                    return Long.parseLong(str);
                }
            }

            log.error("claims中字段类型非法: key={}, value={}, type={}", key, value, value.getClass().getName());
            return null;
        } catch (Exception e) {
            log.error("claims中字段转Long失败: key={}, value={}", key, value, e);
            return null;
        }
    }

    /**
     * 根据userId获取密钥
     */
    private static String getSecretKey(Long userId) {
        int index = Math.toIntExact(userId % SECRET_KEY_POOL.length);
        String baseKey = SECRET_KEY_POOL[index];
        return Base64.getEncoder().encodeToString(baseKey.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) {
        String token =
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqaWFjaGVuZyB5YW5nLiIsImpJZCI6Ijg1NGZhNzE3ZWIwMzQ4ODhhYmE2ZGNjNTJkYjYzYWI4IiwidXNlclR5cGUiOjAsInRva2VuVHlwZSI6ImFjY2VzcyIsImV4cCI6MTc2Nzc2ODk1NywidXNlcklkIjoxLCJpYXQiOjE3Njc3NjgwNTd9.rnYlu4zZlZArAX5nuBLOHT4XUgKz9_eCY5RtbuUorWF-IHosc15SyeyrXy50COi4wZ5h0s9LSfepLVdC0ZRr4g";
        Claims claims = parseToken(token);
        System.out.println(claims);

    }

}
