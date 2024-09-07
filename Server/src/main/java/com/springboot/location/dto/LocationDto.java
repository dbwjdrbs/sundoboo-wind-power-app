package com.springboot.location.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


public class LocationDto {

    @Getter
    @AllArgsConstructor
    @Setter
    @NoArgsConstructor
    public static class Response{

        private long turbineId;

        private long businessId;

        private String korName;

        private String engName;

        private String latitude;

        private String longitude;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd HH:mm:ss")
        private LocalDateTime modifiedAt;


    }
}
