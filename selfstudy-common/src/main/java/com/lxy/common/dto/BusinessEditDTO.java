package com.lxy.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;

/**
 * 系统配置修改DTO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Data
public class BusinessEditDTO implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 4021168379752040334L;

    @NotNull
    private Long id;

    @NotNull
    private String value;

}
