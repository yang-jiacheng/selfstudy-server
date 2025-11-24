package com.lxy.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 密码登录DTO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/11/19 16:19
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginPasswordDTO {

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 设备类型
     */
    private String device;

}
