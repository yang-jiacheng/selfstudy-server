package com.lxy.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 个人信息修改DTO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Data
public class PersonalEditDTO implements java.io.Serializable {

    @NotNull
    private Long id;

    @NotNull
    private String name;

    private String oldPassword;

    private String newPassword;

    private String profilePath;

}
