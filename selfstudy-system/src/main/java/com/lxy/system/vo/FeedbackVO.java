package com.lxy.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxy.common.util.DateCusUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 意见反馈 VO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/21 17:37
 */

@Data
public class FeedbackVO implements Serializable {
    private static final long serialVersionUID = -6416486320539350938L;

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
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date createTime;

    // 回复时间
    @JsonFormat(pattern = DateCusUtil.YYYY_MM_DD_HH_MM_SS, locale = "zh", timezone = "GMT+8")
    private Date replyTime;

    // 用户是否可见 1可见 2不可见
    private Integer status;

    // 回复状态 0未回复 1已回复
    private Integer replyStatus;

    private String name;

    private String phone;

    private String profilePath;

    private String adminName;
}
