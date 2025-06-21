package com.lxy.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnumVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -3057394247976898396L;

    private Integer value;

    private String label;

}
