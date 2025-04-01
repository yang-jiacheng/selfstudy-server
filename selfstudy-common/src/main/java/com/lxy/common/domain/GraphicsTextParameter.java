package com.lxy.common.domain;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2024/12/17 10:27
 * @Version: 1.0
 */
public class GraphicsTextParameter implements java.io.Serializable {
    private static final long serialVersionUID = -9095058999894233263L;

    //字段名
    private String key;

    //字段值
    private String value;

    /*
     * 从图片左上角为原点，x轴向右为正，y轴向下为正
     */
    private Integer x;

    private Integer y;

    private Integer fontSize;

    /*
     * 字体粗细 bold、normal
     */
    private String fontWeight;

    //字体名称 例：微软雅黑
    private String fontKey;

    //字体值 例：Microsoft Yahei
    private String fontValue;

    //字体颜色 例：rgb(0,0,0)
    private String fontColor;

    public GraphicsTextParameter() {
    }

    // 构造函数：深拷贝构造函数
    public GraphicsTextParameter(GraphicsTextParameter other) {
        if (other != null) {
            this.key = other.key;
            this.value = other.value;
            this.x = other.x;
            this.y = other.y;
            this.fontSize = other.fontSize;
            this.fontWeight = other.fontWeight;
            this.fontKey = other.fontKey;
            this.fontValue = other.fontValue;
            this.fontColor = other.fontColor;
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public String getFontKey() {
        return fontKey;
    }

    public void setFontKey(String fontKey) {
        this.fontKey = fontKey;
    }

    public String getFontValue() {
        return fontValue;
    }

    public void setFontValue(String fontValue) {
        this.fontValue = fontValue;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }
}
