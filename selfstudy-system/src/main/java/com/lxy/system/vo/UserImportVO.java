package com.lxy.system.vo;

import com.lxy.system.po.User;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/20 17:50
 * @Version: 1.0
 */
public class UserImportVO extends User {

    private static final long serialVersionUID = -4198086980720256860L;

    private Integer rowIndex;

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public String toString() {
        return "UserImportVO{" +
                "rowIndex=" + rowIndex +
                '}';
    }

}
