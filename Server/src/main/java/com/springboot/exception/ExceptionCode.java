package com.springboot.exception;

import lombok.Getter;


public enum ExceptionCode {
    BUSINESS_NOT_FOUND(404, "Business not found"),
    BUSINESS_EXISTS(409, "Business exists"),
    LOCATION_NOT_FOUND(404, "Location not found"),
    TURBINE_NOT_FOUND(404, "Turbine not found");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}

