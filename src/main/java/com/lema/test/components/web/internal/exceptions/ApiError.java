package com.lema.test.components.web.internal.exceptions;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(Throwable ex) {
        this();
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(String message) {
        this();
        this.message = message;
    }

    public ApiError(String message, Throwable ex) {
        this();
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
}
