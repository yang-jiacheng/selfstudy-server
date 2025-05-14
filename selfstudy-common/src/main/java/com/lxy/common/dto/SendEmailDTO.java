package com.lxy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 邮件发送
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SendEmailDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1392099050306961375L;

    //收件人
    private String recipient;
    //主题
    private String subject;
    //内容
    private String body;

}
