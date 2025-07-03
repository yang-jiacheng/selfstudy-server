package com.lxy.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Data
public class PersonalEditDTO implements java.io.Serializable {

    @NotNull
    private Integer id;

    @NotNull
    private String name;

    private String oldPassword;

    private String newPassword;

    private String profilePath;

}
