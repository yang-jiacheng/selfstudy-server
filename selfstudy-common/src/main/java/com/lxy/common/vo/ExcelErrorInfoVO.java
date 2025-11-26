package com.lxy.common.vo;

import lombok.Data;

/**
 * Excel错误信息VO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2023/02/20 17:48
 */

@Data
public class ExcelErrorInfoVO {

    /**
     * 报错位置
     */
    private String position;
    /**
     * 原因
     */
    private String reason;
    /**
     * 描述
     */
    private String dispose;

    public ExcelErrorInfoVO() {}

    public ExcelErrorInfoVO(String position, String reason, String dispose) {
        this.position = position;
        this.reason = reason;
        this.dispose = dispose;
    }

}
