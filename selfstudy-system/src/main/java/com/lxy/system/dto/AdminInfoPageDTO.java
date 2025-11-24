package com.lxy.system.dto;

import com.lxy.common.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 后管分页查询DTO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminInfoPageDTO extends PageDTO {
    @Serial
    private static final long serialVersionUID = 7688513498743720400L;

    private Long userId;

}
