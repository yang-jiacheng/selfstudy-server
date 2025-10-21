package com.lxy.common.exception;

import java.io.Serial;

/**
 * 业务异常
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/10/21 11:24
 */

public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6101535349935427902L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
