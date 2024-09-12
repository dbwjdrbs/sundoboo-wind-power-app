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
        private long businessId;

        private long turbineId;

        private String businessTitle;

        private String modelName;

        private String latitude;

        private String longitude;

        private String city;

        private String island;

    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch {
        private long locationId;

        private long turbineId;

        private long businessId;

        private String modelName;

        private String latitude;

        private String longitude;

        private String city;

        private String island;

        private LocalDateTime createdAt;

        private LocalDateTime modifiedAt;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
        public static class Response{
            private long businessId;

            private long turbineId;

            private long locationId;

            private String businessTitle;

            private String modelName;

            private String latitude;

            private String longitude;

            private String city;

            private String island;

            private LocalDateTime createdAt;

            private LocalDateTime modifiedAt;

        }
}
