package com.springboot.business.dto;

import com.springboot.utils.validator.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class BusinessDto {
    @Getter
    @AllArgsConstructor
    public static class Post{
        private long businessId;

        @NotSpace
        private String businessName;
    }

    @Getter
    @AllArgsConstructor
    public static class businessResponse{
        private long businessId;
        private String businessName;
        private LocalDateTime createdAt;
    }
}
