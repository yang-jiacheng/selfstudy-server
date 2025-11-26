package com.lxy.common.vo;

import java.io.Serializable;

/**
 * ztree
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/11/09 15:33
 */
public class ZtreeVO implements Serializable {
    private static final long serialVersionUID = 2118864229823679797L;

    private String id;
    private Integer tempId;
    private String pId;
    private String name;
    private String shortName;
    private boolean open = false;
    private boolean nocheck = false;
    private boolean checked = false;

    private Integer sort;

    private String icon;
    private Integer level;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isNocheck() {
        return nocheck;
    }

    public void setNocheck(boolean nocheck) {
        this.nocheck = nocheck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTempId() {
        return tempId;
    }

    public void setTempId(Integer tempId) {
        this.tempId = tempId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
        if (level != null) {
            if (level == 1) {
                this.icon = "../zTree/treeicon/subject.png";
            } else if (level == 2) {
                this.icon = "../zTree/treeicon/class.png";
            } else if (level == 3) {
                this.icon = "../zTree/treeicon/chapter.png";
            } else {
                this.icon = "../zTree/treeicon/job.png";
            }
        }

    }

    @Override
    public String toString() {
        return "{id:" + id + ", pId:" + pId + ", name:'" + name + "', open:" + open + ", nocheck:" + nocheck
            + ", checked:" + checked + ",icon:" + icon + ",level:" + level + "}";
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
