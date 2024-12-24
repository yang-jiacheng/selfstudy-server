package com.lxy.common.vo;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/11/12 14:48
 * @Version: 1.0
 */
public class LayUiResultVO {

    private  Integer code =0;
    private  String msg ="调用成功";

    private Integer count;
    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private LayUiResultVO (){

    }

    public LayUiResultVO(Integer count, Object data) {
        this.count = count;
        this.data = data;
    }
}
