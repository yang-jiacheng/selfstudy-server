package com.lxy.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;

/**
 * 后管用户状态修改DTO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Data
public class AdminStatusDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 3234152406026589882L;

    @NotNull
    private Long id;

    @NotNull
    // 状态 1正常 2禁用
    private Integer status;

}
