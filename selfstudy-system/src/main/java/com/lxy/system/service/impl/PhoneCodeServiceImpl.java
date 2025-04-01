package com.lxy.system.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxy.common.constant.ConfigConstant;
import com.lxy.system.po.PhoneCode;
import com.lxy.system.mapper.PhoneCodeMapper;
import com.lxy.system.service.BusinessConfigService;
import com.lxy.system.service.PhoneCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.system.service.RedisService;
import com.lxy.common.util.DateCusUtil;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.util.SmsUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-12-19
 */
@Service
public class PhoneCodeServiceImpl extends ServiceImpl<PhoneCodeMapper, PhoneCode> implements PhoneCodeService {

    @Resource
    private RedisService redisService;
    @Resource
    private BusinessConfigService businessConfigService;


    @Override
    public boolean checkPhone(String phone) {
        boolean flag = false;

        int maxCount = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstant.PHONE_CODE_NUM));
        String key = RedisKeyConstant.getPhoneSms(phone);
        String value = redisService.getObject(key, String.class);
        int num = 0 ;
        if (value != null){
            num = Integer.parseInt(value);
        }
        if (num < maxCount){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean sendVerificationCode(String phone) {
        String code = SmsUtil.getRandomCode();
        boolean flag = SmsUtil.sendMessage(phone, code);
        if (flag){
            Date now = new Date();
            DateTime offsetMinute = DateUtil.offsetMinute(now, 10);
            PhoneCode phoneCode = new PhoneCode(phone, code,0, now, offsetMinute);
            this.save(phoneCode);
            //添加发送次数
            String key = RedisKeyConstant.getPhoneSms(phone);
            String value = redisService.getObject(key, String.class);
            int num = 0 ;
            if (value != null){
                num = Integer.parseInt(value);
            }
            num += 1;
            redisService.setObject(key,String.valueOf(num), DateCusUtil.getEndByDay(),TimeUnit.SECONDS);
        }
        return flag;
    }

    @Override
    public boolean checkVerificationCode(String phone, String verificationCode) {
        boolean flag = true;
        LambdaQueryWrapper<PhoneCode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PhoneCode::getPhone,phone).eq(PhoneCode::getCode,verificationCode).ne(PhoneCode::getUseStatus,1);
        PhoneCode phoneCode = this.getOne(wrapper);
        Date now = new Date();
        if (phoneCode == null || DateUtil.compare(now,phoneCode.getEndTime()) > 0){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updateVerificationCodeStatus(String phone, String verificationCode) {
        LambdaUpdateWrapper<PhoneCode> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PhoneCode::getCode,verificationCode).eq(PhoneCode::getPhone,phone).set(PhoneCode::getUseStatus,1);
        return this.update(wrapper);
    }
}
