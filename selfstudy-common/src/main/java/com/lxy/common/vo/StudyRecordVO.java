package com.lxy.common.vo;

import com.lxy.common.po.StudyRecord;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/24 16:10
 * @Version: 1.0
 */
public class StudyRecordVO extends StudyRecord implements Serializable {
    private static final long serialVersionUID = -559230604195324125L;

    /**
     * 用户昵称
     */
    private String name;

    private String phone;

    /**
     * 用户头像
     */
    private String profilePath;

    /**
     * 图书馆名称
     */
    private String classifyName;

    /**
     * 自习室名称
     */
    private String catalogName;

    private String parentCatalogName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getParentCatalogName() {
        return parentCatalogName;
    }

    public void setParentCatalogName(String parentCatalogName) {
        this.parentCatalogName = parentCatalogName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
