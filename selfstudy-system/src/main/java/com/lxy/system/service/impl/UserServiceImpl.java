package com.lxy.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.system.po.User;
import com.lxy.system.mapper.UserMapper;
import com.lxy.system.service.OperationLogService;
import com.lxy.system.service.RedisService;
import com.lxy.system.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.util.DateCusUtil;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.system.vo.UserImportVO;
import com.lxy.system.vo.UserRankVO;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private  RedisService redisService;

    @Resource
    private  UserMapper userMapper;

    @Resource
    private OperationLogService operationLogService;


    @Override
    public Page<User> getUserPageList(String name, String phone, String startTime, String endTime, Integer current, Integer size) {
        Page<User> pg = new Page<>(current,size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getId);
        if (StrUtil.isNotBlank(name)){
            wrapper.like(User::getName,name);
        }

        if (StrUtil.isNotBlank(phone)){
            wrapper.like(User::getPhone,phone);
        }

        if (StrUtil.isNotEmpty(startTime)){
            wrapper.gt(User::getCreateTime,startTime);
        }

        if (StrUtil.isNotEmpty(endTime)){
            wrapper.lt(User::getCreateTime,endTime);
        }

        pg = this.page(pg,wrapper);

        return pg;
    }

    @Override
    public boolean saveUser(User user) {
        boolean flag = false;
        Integer id = user.getId();
        if (id == null){
            User user2 = this.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, user.getPhone()));
            if (user2 !=null ){
                return false;
            }
            user.setRegistType(2);
            flag = this.save(user);
        }else {
            User user2 = this.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, user.getPhone()).ne(User::getId,user.getId()));
            if (user2 !=null ){
                return false;
            }
            user.setUpdateTime(new Date());
            flag = this.updateById(user);
            //删缓存
            this.removeUserInfoCache(user.getId());
        }
        return flag;
    }

    @Override
    public User getUserByPhoneAndPassword(String phone, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,phone).eq(User::getPassword,password);
        return this.getOne(wrapper);
    }

    @Override
    public boolean submitStudyDuration(Integer userId, Integer duration) {
        boolean flag = false;
        User user = this.getById(userId);
        if (user != null){
            //总时长累加
            Integer totalDuration = user.getTotalDuration() == null ? 0 : user.getTotalDuration();
            totalDuration += duration;
            user.setTotalDuration(totalDuration);
            //更新用户
            LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(User::getId,user.getId())
                    .set(User::getTotalDuration,totalDuration)
                    .set(User::getUpdateTime,new Date());
            flag = this.update(wrapper);
            //删缓存
            this.removeUserInfoCache(userId);
        }
        return flag;
    }

    @Override
    public User getUserInfo(Integer userId) {
        User user = this.getUserInfoCache(userId);
        if (user != null){
            return user;
        }
        user = this.getById(userId);
        if (user != null){
            user.setPassword("");
            user.setProfilePath(ImgConfigUtil.joinUploadUrl(user.getProfilePath()));
            user.setCoverPath(ImgConfigUtil.joinUploadUrl(user.getCoverPath()));
            //加缓存
            this.insertUserInfoCache(user);
        }
        return user;
    }

    @Override
    public User getUserInfoCache(Integer userId) {
        String key = RedisKeyConstant.getUserInfo(userId);
        User user = redisService.getObject(key, User.class);
        return user;
    }

    @Override
    public void removeUserInfoCache(Integer userId) {
        String key = RedisKeyConstant.getUserInfo(userId);

        redisService.deleteKey(key);

    }

    @Override
    public void removeUserInfoCacheByIds(List<Integer> ids) {
        if (CollUtil.isNotEmpty(ids)){
            Set<String> keys = new HashSet<>(ids.size());

            ids.forEach(id -> {
                keys.add(RedisKeyConstant.getUserInfo(id));

            });
            redisService.deleteKeys(keys);

        }
    }

    @Override
    public void insertUserInfoCache(User user) {
        String key = RedisKeyConstant.getUserInfo(user.getId());
        //缓存 7天
        redisService.setObject(key, user,604800L, TimeUnit.SECONDS);
    }

    @Override
    public List<UserRankVO> getRankingsTotalDuration() {
        List<UserRankVO> list = this.getRankingsTotalDurationCache();
        if (CollUtil.isNotEmpty(list)){
            return list;
        }
        list = this.getRankingsTotalDurationInDb();
        return list;
    }

    @Override
    public List<UserRankVO> getRankingsTotalDurationInDb() {
        List<UserRankVO> list = userMapper.getRankingsTotalDuration();
        if (CollUtil.isNotEmpty(list)){
            //移除0学习时长
            list.removeIf(user -> user.getTotalDuration() == null || user.getTotalDuration() == 0);
            //按时长倒序
            list.sort((t1,t2) -> t2.getTotalDuration().compareTo(t1.getTotalDuration()));
            //设置用户头像和排名
            UserRankVO rankVO = null;
            for (int i = 0; i < list.size(); i++) {
                rankVO = list.get(i);
                rankVO.setProfilePath(ImgConfigUtil.joinUploadUrl(rankVO.getProfilePath()));
                rankVO.setRanking(i+1);
                list.set(i,rankVO);
            }
        }
        return list;
    }

    @Override
    public void insertRankingsCache(List<UserRankVO> users){
        //缓存到今天结束
        long endByDay = DateCusUtil.getEndByDay();
        String key = RedisKeyConstant.getRankings();
        redisService.setObject(key,users,endByDay,TimeUnit.SECONDS);
    }

    @Override
    public UserRankVO getUserRankingById(Integer userId) {
        String studyDay = DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
        UserRankVO user = userMapper.getUserRankingById(userId, studyDay);
        user.setProfilePath(ImgConfigUtil.joinUploadUrl(user.getProfilePath()));
        return user;
    }

    @Override
    public void insertBatchUser(List<UserImportVO> userList) {
        userMapper.insertBatchUser(userList);
    }


    @Override
    public void test() {
        UserServiceImpl proxy = (UserServiceImpl)AopContext.currentProxy();
        proxy.b();
        throw new RuntimeException("test");
    }

    @Transactional(rollbackFor = Exception.class)
    public void b(){
        User user = new User();
        user.setId(108);
        user.setName("test");
        this.updateById(user);
    }

    private List<UserRankVO> getRankingsTotalDurationCache(){
        String key = RedisKeyConstant.getRankings();
        List<UserRankVO> list = redisService.getObject(key, ArrayList.class);
        return list;
    }



}
