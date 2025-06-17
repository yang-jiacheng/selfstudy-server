package com.lxy.system.dto;

import com.lxy.system.po.AdminInfo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Data
public class AdminEditDTO {

    @NotNull
    private AdminInfo adminInfo;

    @NotEmpty
    private List<Integer> roleIds;

}
