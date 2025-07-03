package com.lxy.system.dto;

import com.lxy.common.dto.PageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackPageDTO extends PageDTO {

    @Serial
    private static final long serialVersionUID = -1248414479402536100L;

    private String phone;

    private String content;

    private Integer replyStatus;

    private Integer userId;

    private Integer status;

}
