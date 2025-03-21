package com.lxy.common.security.serviice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.lxy.common.security.bo.StatelessUser;
import com.lxy.common.service.AdminInfoService;
import com.lxy.common.service.RedisService;
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
    @Resource
    private AdminInfoService adminInfoService;

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
     * 更新权限信息
     * 从缓存中获取权限，若缓存中没有则才正常从数据库中获取
     * 注意：如果用户权限发生改变时，需要将缓存中的数据删除
     * @author jiacheng yang.
     * @since 2025/03/06 18:57
     */
    public void updatePermissions(String key,Integer userId, StatelessUser loginStatus){
        if (CollUtil.isEmpty(loginStatus.getPermissions())){
            //根据用户查询权限信息 添加到StatelessUser中
            List<String> permissions = adminInfoService.getPermissionsById(userId);
            loginStatus.setPermissions(permissions);
            //修改缓存里的权限
            List<StatelessUser> loginList = redisService.getObject(key, ArrayList.class);
            for (StatelessUser statelessUser : loginList) {
                statelessUser.setPermissions(permissions);
            }
            redisService.setObject(key, loginList, -1L, null);
        }
    }


}
