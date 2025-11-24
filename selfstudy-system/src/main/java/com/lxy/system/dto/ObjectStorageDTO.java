package com.lxy.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 对象存储DTO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2024/12/14 16:19
 */

@Data
public class ObjectStorageDTO implements Serializable {
    private static final long serialVersionUID = -6584352603005168007L;

    @NotNull(message = "文件名不能为空")
    private String fileName;

    @NotNull(message = "文件路径不能为空")
    private String downloadUrl;

    @NotNull(message = "文件大小不能为空")
    private BigDecimal fileSize;

}
