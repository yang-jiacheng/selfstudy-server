package com.lxy.system.dto;

import com.lxy.common.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 学习记录分页查询DTO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class StudyRecordPageDTO extends PageDTO {
    @Serial
    private static final long serialVersionUID = 3826989770979060513L;

    // 手机号
    private String phone;

    // 图书馆
    private Long classifyId;

    // 状态（1自习中 3已完成）
    private Integer status;

}
