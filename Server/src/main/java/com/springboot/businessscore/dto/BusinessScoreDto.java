package com.springboot.businessscore.dto;

import com.springboot.utils.validator.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class BusinessScoreDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Post{

        @Positive
        private long businessId;
        @NotNull
        @NotSpace
        @Pattern(regexp = "^[가-힣]{1,20}$", message = "BusinessTitle은 1자에서 20자 사이의 한글만 입력할 수 있습니다.")
        private String businessScoreTitle;
        @Range(min = 1, max = 5)
        private int scoreList1;
        @Range(min = 1, max = 5)
        private int scoreList2;
        @Range(min = 1, max = 5)
        private int scoreList3;
        @Range(min = 1, max = 5)
        private int scoreList4;
        @NotSpace
        @NotNull
        private String observerName;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private long businessId;

        private long businessScoreId;

        private String businessScoreTitle;

        private int ScoreList1;

        private int ScoreList2;

        private int ScoreList3;

        private int ScoreList4;

        private String observerName;

        private LocalDateTime createdAt;

        private LocalDateTime modifiedAt;
    }

}
