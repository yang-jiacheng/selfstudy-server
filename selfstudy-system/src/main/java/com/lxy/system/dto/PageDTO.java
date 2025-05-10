package com.lxy.system.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;

/**
 * 分页查询基础对象
 * @author jiacheng yang.
 * @since 2024/12/14 15:59
 * @version 1.0
 */

@Data
public class PageDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -2335352111793861831L;

    private Integer page;

    private Integer limit;

    private String name;

    private Integer current;

    public void setPage(Integer page) {
        this.page = page;
        if (page != null && limit != null) {
            current = (page - 1) * limit;
        }
    }


}
