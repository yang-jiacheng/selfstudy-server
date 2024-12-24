package com.lxy.common.vo;

import java.io.Serializable;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/01/13 10:37
 * @Version: 1.0
 */
public class RoomVO implements Serializable {
    private static final long serialVersionUID = -9081562248724816519L;

    private Integer id;
    private Integer parentId;
    private String name;
    private Integer personCount;
    private String parentName;
    private String libraryName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    @Override
    public String toString() {
        return "RoomVO{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", personCount=" + personCount +
                ", parentName='" + parentName + '\'' +
                ", libraryName='" + libraryName + '\'' +
                '}';
    }
}
