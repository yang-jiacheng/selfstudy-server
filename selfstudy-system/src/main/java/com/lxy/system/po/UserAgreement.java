package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * Description: 用户协议与隐私政策
 * author: jiacheng yang.
 * Date: 2025-02-19
 */

public class UserAgreement implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //1 隐私政策 2用户协议
    private Integer type;

    private String title;

    //内容
    private String content;

    public Integer getId() {
    	return id;
    }

    public void setId(Integer id) {
    	this.id = id;
    }

    public Integer getType() {
    	return type;
    }

    public void setType(Integer type) {
    	this.type = type;
    }

    public String getTitle() {
    	return title;
    }

    public void setTitle(String title) {
    	this.title = title;
    }

    public String getContent() {
    	return content;
    }

    public void setContent(String content) {
    	this.content = content;
    }


    @Override
    public String toString() {
	    return "UserAgreement{" +
	            ", id=" + id +
	            ", type=" + type +
	            ", title=" + title +
	            ", content=" + content +
	    "}";
    }

}
