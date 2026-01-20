package com.lxy.common.vo;

import com.lxy.common.vo.base.BaseTreeVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 自习室树形结构
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@EqualsAndHashCode(callSuper = true)
@Data

public class CatalogTreeVO extends BaseTreeVO<CatalogTreeVO> {
    @Serial
    private static final long serialVersionUID = -6714104747783083093L;

    private Integer level;

    private String name;

    private Integer sort;

}
