package com.lxy.system.vo.user;

import com.lxy.system.po.User;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2023/02/20 17:50
 * @version 1.0
 */
public class UserImportVO extends User {

    private static final long serialVersionUID = -4198086980720256860L;

    private Integer rowIndex;

    private Integer sheetIndex;

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(Integer sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    @Override
    public String toString() {
        return "UserImportVO{" +
                "rowIndex=" + rowIndex +
                '}';
    }

}
