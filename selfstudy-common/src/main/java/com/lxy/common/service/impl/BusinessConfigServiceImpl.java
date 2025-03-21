package com.lxy.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxy.common.mapper.BusinessConfigMapper;
import com.lxy.common.po.BusinessConfig;
import com.lxy.common.service.BusinessConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.service.RedisService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 业务配置表 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
@Service
public class BusinessConfigServiceImpl extends ServiceImpl<BusinessConfigMapper, BusinessConfig> implements BusinessConfigService {

    @Resource
    private RedisService redisService;

    @Override
    public String getBusinessConfigValue(String key) {
        String value = getBusinessConfigCacheByKey(key);
        if (StrUtil.isEmpty(value)){
            BusinessConfig businessConfig = getBusinessConfigByKey(key);
            if (businessConfig!=null){
                value = businessConfig.getBvalue();
                updateBusinessConfigCacheByKey(key,value);
            }
        }
        return value;
    }

    @Override
    public BusinessConfig getBusinessConfigByKey(String key) {
        return this.getOne(new QueryWrapper<BusinessConfig>().eq("bkey", key));
    }

    @Override
    public BusinessConfig updateBusinessConfigByKey(String key, String value, String description) {
        BusinessConfig config = getBusinessConfigByKey(key);
        if (config==null){
            config = new BusinessConfig();
        }
        config.setBkey(key);
        config.setBvalue(value);
        config.setDescription(description);
        this.saveOrUpdate(config);
        updateBusinessConfigCacheByKey(key,value);
        return config;
    }

    @Override
    public void updateBusinessConfigById(Integer id, String value) {
        BusinessConfig con = this.getById(id);
        if (con == null){
            return;
        }
        BusinessConfig config = new BusinessConfig();
        config.setId(id);
        config.setBvalue(value);
        this.updateById(config);
        updateBusinessConfigCacheByKey(con.getBkey(),value);
    }

    private String getBusinessConfigCacheByKey(String key){
        String cacheKey = RedisKeyConstant.getBusinessConfig(key);
        return redisService.getObject(cacheKey,String.class);
    }

    private void updateBusinessConfigCacheByKey(String key,String value){
        if(StrUtil.isEmpty(key) || StrUtil.isEmpty(value)){
            return ;
        }
        String cacheKey = RedisKeyConstant.getBusinessConfig(key);
        redisService.setObject(cacheKey,value, 7L, TimeUnit.DAYS);
    }
}
