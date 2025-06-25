package com.lxy.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
public class AgreementEditDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5680572052160514235L;

    @NotNull
    private Integer type;

    private String content;

}
