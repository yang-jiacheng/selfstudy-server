package com.lxy.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;

/**
 * TODO
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
    private Integer id;

    @NotNull
    //状态 1正常 2禁用
    private Integer status;

}
