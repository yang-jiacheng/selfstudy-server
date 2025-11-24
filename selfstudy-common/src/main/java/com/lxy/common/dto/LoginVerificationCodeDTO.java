package com.lxy.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 验证码登录DTO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/11/19 16:16
 */

@Data
public class LoginVerificationCodeDTO {

    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 设备类型
     */
    private String device;

}
