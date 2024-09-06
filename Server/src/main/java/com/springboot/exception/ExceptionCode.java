package com.springboot.exception;

import lombok.Getter;


public enum ExceptionCode {
    BUSINESS_NOT_FOUND(404, "Business not found");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}

