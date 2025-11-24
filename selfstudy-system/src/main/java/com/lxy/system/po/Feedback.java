package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 意见反馈
 *
 * @author jiacheng yang.
 * @since 2025-02-19
 */

@Data
@NoArgsConstructor
@TableName(value = "feedback", autoResultMap = true)
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 用户id
    private Long userId;

    // 反馈内容
    private String content;

    // 反馈图片
    private String pic;

    // 回复内容
    private String reply;

    // 回复人
    private Long adminId;

    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    // 回复时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date replyTime;

    // 用户是否可见 1可见 2不可见
    private Integer status;

    // 回复状态 0未回复 1已回复
    private Integer replyStatus;

    public Feedback(Long userId, String content, String pic, Date createTime, Integer status, Integer replyStatus) {
        this.userId = userId;
        this.content = content;
        this.pic = pic;
        this.createTime = createTime;
        this.status = status;
        this.replyStatus = replyStatus;
    }

}
