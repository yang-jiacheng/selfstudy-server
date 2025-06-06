package com.lxy.system.dto;

import com.lxy.common.domain.GraphicsTextParameter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2024/12/17 10:52
 * @version 1.0
 */
public class GenerateImageDTO implements java.io.Serializable {
    private static final long serialVersionUID = 2435298421128860208L;

    @NotNull(message = "图片地址不能为空")
    private String url;

    @NotNull(message = "模板参数不能为空")
    @Size(min = 1, message = "模板参数不能为空")
    private List<GraphicsTextParameter> parameters;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<GraphicsTextParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<GraphicsTextParameter> parameters) {
        this.parameters = parameters;
    }
}
