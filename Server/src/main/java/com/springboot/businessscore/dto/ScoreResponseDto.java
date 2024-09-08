package com.springboot.businessscore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoreResponseDto {

    @Column(nullable = false)
    private long businessId;

    @Column(nullable = false, length = 20)
    private String businessScoreTitle;

    private int ScoreList1;

    private int ScoreList2;

    private int ScoreList3;

    private int ScoreList4;

    @Column(nullable = false, length = 20)
    private String observerName;

    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt;
}
