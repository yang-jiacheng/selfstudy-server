package com.lxy.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Label和Value VO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/11/24 17:47
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelValueVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7712076390127092258L;

    private Long value;

    private String label;

}
