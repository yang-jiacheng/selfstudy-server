package com.lxy.system.dto;


import jakarta.validation.constraints.NotNull;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2024/12/14 15:59
 * @Version: 1.0
 */
public class PageDTO implements java.io.Serializable {

    @NotNull
    private Integer page;

    @NotNull
    private Integer limit;

    private String name;

    private Integer current;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
        if (page != null && limit != null) {
            current = (page - 1) * limit;
        }
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCurrent() {
        return current;
    }
}
