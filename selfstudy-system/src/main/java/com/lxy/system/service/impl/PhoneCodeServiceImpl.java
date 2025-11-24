package com.lxy.system.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.constant.ConfigConstant;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.constant.SmsConstant;
import com.lxy.common.constant.dict.UseStatusConstant;
import com.lxy.common.dto.SmsSendDTO;
import com.lxy.common.exception.ServiceException;
import com.lxy.common.util.DateCusUtil;
import com.lxy.common.util.SmsUtil;
import com.lxy.common.util.SmsVerifyCodeUtil;
import com.lxy.system.mapper.PhoneCodeMapper;
import com.lxy.system.po.PhoneCode;
import com.lxy.system.service.BusinessConfigService;
import com.lxy.system.service.PhoneCodeService;
import com.lxy.system.service.redis.RedisService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
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
        int num = 0;
        if (value != null) {
            num = Integer.parseInt(value);
        }
        if (num < maxCount) {
            flag = true;
        }
        return flag;
    }

    @Override
    public void sendVerificationCode(String phone) {
        boolean checkFlag = this.checkPhone(phone);
        if (!checkFlag) {
            throw new ServiceException("今日验证码次数已到上限 5条！");
        }
        String code = SmsUtil.getRandomCode();
        List<SmsSendDTO.TemplateParam> params = new ArrayList<>();
        params.add(new SmsSendDTO.TemplateParam(SmsVerifyCodeUtil.TEMP_CODE, code));
        params.add(new SmsSendDTO.TemplateParam(SmsVerifyCodeUtil.TEMP_MIN, SmsConstant.EXPIRE_MINUTES.toString()));
        SmsSendDTO smsSendDTO = new SmsSendDTO(null, params);
        boolean flag = SmsVerifyCodeUtil.sendMessage(phone, smsSendDTO);
        if (flag) {
            Date now = new Date();
            DateTime offsetMinute = DateUtil.offsetMinute(now, SmsConstant.EXPIRE_MINUTES);
            PhoneCode phoneCode = new PhoneCode(phone, code, UseStatusConstant.UNUSED, now, offsetMinute);
            this.save(phoneCode);
            // 添加发送次数
            String key = RedisKeyConstant.getPhoneSms(phone);
            String value = redisService.getObject(key, String.class);
            int num = 0;
            if (value != null) {
                num = Integer.parseInt(value);
            }
            num += 1;
            redisService.setObject(key, String.valueOf(num), DateCusUtil.getEndByDay(), TimeUnit.SECONDS);
        } else {
            throw new ServiceException("短信发送失败，请稍后重试");
        }
    }

    @Override
    public boolean checkVerificationCode(String phone, String verificationCode) {
        boolean flag = true;
        LambdaQueryWrapper<PhoneCode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PhoneCode::getPhone, phone).eq(PhoneCode::getCode, verificationCode).ne(PhoneCode::getUseStatus,
            UseStatusConstant.USED);
        PhoneCode phoneCode = this.getOne(wrapper);
        Date now = new Date();
        if (phoneCode == null || DateUtil.compare(now, phoneCode.getEndTime()) > 0) {
            flag = false;
        }
        return flag;
    }

    @Override
    public void updateVerificationCodeStatus(String phone, String verificationCode) {
        LambdaUpdateWrapper<PhoneCode> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PhoneCode::getCode, verificationCode).eq(PhoneCode::getPhone, phone).set(PhoneCode::getUseStatus,
            UseStatusConstant.USED);
        this.update(wrapper);
    }
}
