package com.springboot.business.dto;

import com.springboot.utils.validator.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class BusinessDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Post{
        @NotSpace
        private String businessTitle;
    }

    @Getter
    @AllArgsConstructor
    public static class Response{
        private long businessId;
        private String businessTitle;
        private LocalDateTime createdAt;
        private LocalDateTime deletedAt;
//        private List<TurbineDto.Response> Turbines;
//        private List<BusinessScoreDto.Response> businessScore;
    }

}
