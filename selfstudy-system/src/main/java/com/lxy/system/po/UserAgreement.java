package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户协议与隐私政策
 *
 * @author jiacheng yang.
 * @since 2025-02-19
 */

@Data
@TableName(value = "user_agreement", autoResultMap = true)
public class UserAgreement implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 1 隐私政策 2用户协议
    private Integer type;

    private String title;

    // 内容
    private String content;

}
