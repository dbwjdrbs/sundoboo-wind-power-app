package com.springboot.exception;

import lombok.Getter;


public enum ExceptionCode {
    BUSINESS_NOT_FOUND(404, "Business not found"),
    OBSERVER_ALREADY_EVALUATION(404, "Observer already evaluation"),
    BUSINESS_SCORE_TITLE_ALREADY_EXISTS(404, "BusinessScore title already exists"),
    BUSINESS_ALREADY_EXISTS(404,"Business Title already exists");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}

