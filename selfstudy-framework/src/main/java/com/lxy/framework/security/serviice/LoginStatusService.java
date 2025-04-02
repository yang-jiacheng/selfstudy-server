package com.lxy.framework.security.serviice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.lxy.framework.security.domain.StatelessUser;
import com.lxy.system.service.RedisService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 登录状态服务
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Service
public class LoginStatusService {

    @Resource
    private RedisService redisService;

    public void removeInRedis(String key, String token) {
        List<StatelessUser> loginList = redisService.getObject(key, ArrayList.class);
        if (CollUtil.isNotEmpty(loginList)){
            loginList.removeIf(o -> o.getToken().equals(token));
            if (loginList.isEmpty()){
                redisService.deleteKey(key);
            }else {
                redisService.setObject(key, loginList, -1L, null);
            }
        }
    }

    /**
     * 登录状态持久化到redis
     * @param statelessUser 用户信息
     * @param endDay 过期时长单位天
     */
    public void loginStatusToRedis(String key, StatelessUser statelessUser, int endDay){
        Date now = new Date();
        statelessUser.setLoginTime(now);

        Date end = DateUtil.offsetDay(now, endDay);
        //设置过期时间
        statelessUser.setEndTime(end);
        List<StatelessUser> loginList = redisService.getObject(key, ArrayList.class);
        if (CollUtil.isEmpty(loginList)){
            loginList = new ArrayList<>(1);
        }
        loginList.add(statelessUser);
        redisService.setObject(key, loginList, -1L, null);
    }

    /**
     * 控制一个账号在线数
     * @author jiacheng yang.
     * @since 2025/03/06 18:55
     * @param key 缓存key
     * @param onlineNum 一个账号在线数
     * @param endDay 在线时长 天
     * @param token token
     */
    public StatelessUser controlLoginNum(String key, Integer onlineNum, int endDay, String token){
        Date now = new Date();

        StatelessUser loginStatus = null;
        if (redisService.hasKey(key)){
            List<StatelessUser> loginList = redisService.getObject(key, ArrayList.class);
            if (CollUtil.isNotEmpty(loginList)){
                //删除过期的jwt
                loginList.removeIf(o -> DateUtil.compare(now, o.getEndTime()) > 0);
                //按过期时间正序
                loginList.sort(Comparator.comparing(StatelessUser::getEndTime));
                int size = loginList.size();
                if (size > onlineNum){
                    loginList.remove(0);
                }
                for (StatelessUser status : loginList) {
                    String accessToken = status.getToken();
                    if (token.equals(accessToken)){
                        Date end = DateUtil.offsetDay(now, endDay);
                        status.setEndTime(end);
                        loginStatus = status;
                        break;
                    }
                }
                redisService.setObject(key, loginList, -1L, null);
            }
        }
        return loginStatus;
    }

    /**
     * 获取登录状态
     * @author jiacheng yang.
     * @since 2025/4/2 16:36
     */
    public StatelessUser getLoginStatus(String key,String token){
        StatelessUser loginStatus = null;
        if (redisService.hasKey(key)) {
            List<StatelessUser> loginList = redisService.getObject(key, ArrayList.class);
            if (CollUtil.isEmpty(loginList)) {
                return null;
            }

            loginStatus = loginList.stream()
                    .filter(status -> token.equals(status.getToken()))
                    .findFirst()
                    .orElse(null);
        }
        return loginStatus;
    }

}
