package com.springboot.businessscore.dto;

import com.springboot.utils.validator.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class BusinessScoreDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Post{

        @Positive
        private long businessId;
        @NotNull
        @NotSpace
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




}
