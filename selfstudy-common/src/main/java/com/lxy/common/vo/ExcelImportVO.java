package com.lxy.common.vo;

import lombok.Data;

import java.io.Serial;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/10/21 11:34
 */

@Data
public class ExcelImportVO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -4140829221455657324L;
    /**
     * Excel行索引（从0开始）
     */
    private Integer rowIndex;

    /**
     * Excel Sheet索引
     */
    private Integer sheetIndex;

}
