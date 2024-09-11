package com.springboot.business.dto;

import com.springboot.businessscore.dto.BusinessScoreDto;
import com.springboot.location.dto.LocationDto;
import com.springboot.utils.validator.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class BusinessDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Post{
        @NotSpace
        private String businessTitle;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private long businessId;
        private String businessTitle;
        private LocalDateTime createdAt;
        private LocalDateTime deletedAt;
        private List<LocationDto.Response> locations;
        private List<BusinessScoreDto.Response> businessScores;
    }

}
