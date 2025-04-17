package com.lxy.common.vo;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

public class SmsSendVO implements Serializable {
    private static final long serialVersionUID = 5967400312216805662L;
    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板参数列表
     */
    private List<TemplateParam> templateParams;

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public List<TemplateParam> getTemplateParams() {
        return templateParams;
    }

    public void setTemplateParams(List<TemplateParam> templateParams) {
        this.templateParams = templateParams;
    }


    public static class TemplateParam{
        private String name;
        private String value;

        public TemplateParam() {
        }

        public TemplateParam(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
