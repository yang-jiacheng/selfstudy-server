package com.lxy.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 手机验证码实体
 *
 * @author jiacheng yang.
 * @since 2025-02-19
 */

@Data
@NoArgsConstructor
@TableName(value = "phone_code", autoResultMap = true)
public class PhoneCode implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String phone;

    private String code;

    // 使用状态，字典：`use_status`
    private String useStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    // 过期时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date endTime;

    public PhoneCode(String phone, String code, String useStatus, Date createTime, Date endTime) {
        this.phone = phone;
        this.code = code;
        this.useStatus = useStatus;
        this.createTime = createTime;
        this.endTime = endTime;
    }

}
