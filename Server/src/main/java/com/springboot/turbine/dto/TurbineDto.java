package com.springboot.turbine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class TurbineDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response{
        private String modelName;
    }
}
