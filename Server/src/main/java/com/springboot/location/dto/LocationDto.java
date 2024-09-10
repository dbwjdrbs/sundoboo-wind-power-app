package com.springboot.location.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class LocationDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Post{
        @Column(nullable = false, length = 20)
        private long businessId;

        // 이거는 후순위로 넣는건가? 애매띠
        @Column(nullable = false, length = 20)
        private long turbineId;

        private String businessTitle;

        private String modelName;

        @Column(length = 100)
        private String latitude;

        @Column(length = 100)
        private String longitude;

        @Column(length = 20)
        private String city;

        @Column(length = 20)
        private String island;

        private LocalDateTime deletedAt;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch {
        @Column(nullable = false, length = 20)
        private long locationId;

        // 이거는 후순위로 넣는건가? 애매띠
        @Column(nullable = false, length = 20)
        private long turbineId;

        @Column(nullable = false)
        private long businessId;

        private String modelName;

        @Column(length = 100)
        private String latitude;

        @Column(length = 100)
        private String longitude;

        @Column(length = 20)
        private String city;

        @Column(length = 20)
        private String island;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        @Column(nullable = false, length = 20)
        private long businessId;

        // 이거는 후순위로 넣는건가? 애매띠
        @Column(nullable = false, length = 20)
        private long turbineId;

        @Column(nullable = false, length = 20)
        private long locationId;

        private String businessTitle;

        private String modelName;

        @Column(length = 100)
        private String latitude;

        @Column(length = 100)
        private String longitude;

        @Column(length = 20)
        private String city;

        @Column(length = 20)
        private String island;

        private LocalDateTime deletedAt;
    }
}
