package com.lxy.common.vo;

import java.io.Serial;
import java.io.Serializable;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

public class EnumVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -3057394247976898396L;

    private Integer type;

    private String name;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnumVO() {
    }

    public EnumVO(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return "EnumVO{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
