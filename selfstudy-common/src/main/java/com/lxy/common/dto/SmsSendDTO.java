package com.lxy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 短信发送DTO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SmsSendDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5967400312216805662L;
    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板参数列表
     */
    private List<TemplateParam> templateParams;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TemplateParam {
        private String name;
        private String value;
    }
}
