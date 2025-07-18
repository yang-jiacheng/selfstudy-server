package com.lxy.system.vo;

import com.lxy.system.po.AdminInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/7/16 19:25
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminInfoVO extends AdminInfo {
    @Serial
    private static final long serialVersionUID = 6034862826429517648L;

    private List<String> permissions;

}
