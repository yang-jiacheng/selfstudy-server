package com.lxy.common.domain;

import java.io.Serial;
import java.io.Serializable;

/**
 * 返回结果对象
 * @author  jiacheng yang.
 * @since  2024/01/24 11:23
 * @version 1.0
 */
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -7393525156672911665L;

    /**
     * 成功
     */
    public static final int SUCCESS = 0;

    /**
     * 失败
     */
    public static final int FAIL = -1;


    private int code;

    private String msg;

    private T result;

    public R() {
    }

    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(T result) {
        return restResult(result, SUCCESS, "操作成功");
    }

    public static <T> R<T> fail() {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> R<T> restResult(T result, int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setResult(result);
        r.setMsg(msg);
        return r;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
