package com.lxy.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 后管用户修改DTO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Data
public class AdminEditDTO {

    @NotNull
    private AdminInfoUpdateDTO adminInfo;

    @NotEmpty
    private List<Long> roleIds;

}
