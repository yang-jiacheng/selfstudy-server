package com.lxy.system.vo;

import java.io.Serializable;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/12/24 11:12
 * @version 1.0
 */
public class CatalogVO implements Serializable {
    private static final long serialVersionUID = 162687177993801603L;

    /**
     * 自习室id
     */
    private Integer catalogId;

    /**
     * 图书馆id
     */
    private Integer classifyId;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 层级 1楼层 2自习室
     */
    private Integer level;

    /**
     * 昵称
     */
    private String catalogName;

    /**
     * 容纳人数
     */
    private Integer personCount;

    /**
     * 当前人数
     */
    private Integer currCount;

    /**
     * 排序
     */
    private Integer sort;

    public Integer getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Integer catalogId) {
        this.catalogId = catalogId;
    }

    public Integer getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Integer classifyId) {
        this.classifyId = classifyId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public Integer getCurrCount() {
        return currCount;
    }

    public void setCurrCount(Integer currCount) {
        this.currCount = currCount;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "CatalogVO{" +
                "catalogId=" + catalogId +
                ", classifyId=" + classifyId +
                ", parentId=" + parentId +
                ", level=" + level +
                ", catalogName='" + catalogName + '\'' +
                ", personCount=" + personCount +
                ", currCount=" + currCount +
                ", sort=" + sort +
                '}';
    }
}
