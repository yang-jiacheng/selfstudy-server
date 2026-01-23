package com.lxy.common.model;


import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页结果
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/03/06 14:52
 */

public class PageResult<T> {
    /**
     * 当前页码
     */
    private Integer current = 1;
    /**
     * 每页显示的信息条数
     */
    private Integer size = 10;
    /**
     * 总的信息条数
     */
    private Long total = 0L;
    /**
     * 总的页数
     */
    private Integer pages = 0;
    /**
     * 数据集合
     */
    private List<T> records;

    public PageResult() {
    }

    public PageResult(Integer current, Integer size, Long total, Integer pages, List<T> records) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.pages = pages;
        this.records = records;
    }

    public PageResult(Integer current, Integer size, Long total, List<T> records) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.records = records;
        this.pages = (int) ((total + size - 1) / size);
    }

    /**
     * 静态工厂方法：从  com.github.pagehelper.PageInfo 转换为 PageResult
     */
    public static <T> PageResult<T> convert(PageInfo<T> pageInfo) {
        return new PageResult<>(
                pageInfo.getPageNum(),
                pageInfo.getPageSize(),
                pageInfo.getTotal(),
                pageInfo.getPages(),
                pageInfo.getList()
        );
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
