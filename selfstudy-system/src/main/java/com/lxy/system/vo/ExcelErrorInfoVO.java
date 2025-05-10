package com.lxy.system.vo;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2023/02/20 17:48
 * @version 1.0
 */
public class ExcelErrorInfoVO {

    /**
     * 报错位置
     */
    private String position;
    /**
     * 原因
     */
    private String reason;
    /**
     * 描述
     */
    private String dispose;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDispose() {
        return dispose;
    }

    public void setDispose(String dispose) {
        this.dispose = dispose;
    }

    public ExcelErrorInfoVO() {
    }

    public ExcelErrorInfoVO(String position, String reason, String dispose) {
        this.position = position;
        this.reason = reason;
        this.dispose = dispose;
    }

    @Override
    public String toString() {
        return "ExcelErrorInfoVO{" +
                "position='" + position + '\'' +
                ", reason='" + reason + '\'' +
                ", dispose='" + dispose + '\'' +
                '}';
    }

}
