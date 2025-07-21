package com.lxy.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 双Token机制的Token对象
 * @author jiacheng yang.
 * @since 2025/01/18 10:00
 * @version 1.0
 */

@Data
public class TokenPair implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String refreshId;

    /**
     * 访问Token - 短期有效(15分钟)
     */
    private String accessToken;

    /**
     * 刷新Token - 长期有效(7天)
     */
    private String refreshToken;

    /**
     * 访问Token过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date accessExpires;

    /**
     * 刷新Token过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date refreshExpires;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    /**
     * 最后活跃时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date lastActiveTime;

    /**
     * IP地址
     */
    private String clientIp;

    public TokenPair() {
        this.createTime = new Date();
        this.lastActiveTime = new Date();
    }

    public TokenPair(String refreshId, String accessToken, String refreshToken, Date accessExpires, Date refreshExpires) {
        this();
        this.refreshId = refreshId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessExpires = accessExpires;
        this.refreshExpires = refreshExpires;
    }

}
