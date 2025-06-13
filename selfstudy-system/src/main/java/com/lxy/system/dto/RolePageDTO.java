package com.lxy.system.dto;

import com.lxy.common.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class RolePageDTO extends PageDTO {
    @Serial
    private static final long serialVersionUID = 1894314669322091770L;

    private String startTime;

    private String endTime;

}
