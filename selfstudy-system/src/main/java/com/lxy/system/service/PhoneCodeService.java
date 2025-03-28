package com.lxy.system.service;

import com.lxy.system.po.PhoneCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-12-19
 */
public interface PhoneCodeService extends IService<PhoneCode> {

    /**
     * 检验是否可发验证码
     */
    boolean checkPhone(String phone);

    /**
     * 发送验证码
     */
    boolean sendVerificationCode(String phone);

    /**
     * 验证码是否可用
     */
    boolean checkVerificationCode(String phone,String verificationCode);

    /**
     * 修改验证码为已使用
     */
    boolean updateVerificationCodeStatus(String phone,String verificationCode);

}
