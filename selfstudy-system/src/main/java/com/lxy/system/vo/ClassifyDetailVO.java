package com.lxy.system.vo;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/12/24 11:08
 * @version 1.0
 */
public class ClassifyDetailVO implements Serializable {

    private static final long serialVersionUID = 7065295037862538696L;

    /**
     *  图书馆id
     */
    private Integer id;

    /**
     * 图书馆昵称
     */
    private String name;

    /**
     * 图书馆描述
     */
    private String description;

    /**
     * 头像地址
     */
    private String iconPath;

    /**
     * 封面地址
     */
    private String coverPath;

    /**
     * 自习室
     */
    private List<CatalogVO> rooms;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public List<CatalogVO> getRooms() {
        return rooms;
    }

    public void setRooms(List<CatalogVO> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "ClassifyDetailVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", iconPath='" + iconPath + '\'' +
                ", coverPath='" + coverPath + '\'' +
                ", rooms=" + rooms +
                '}';
    }
}
