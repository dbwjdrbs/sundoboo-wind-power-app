package com.springboot.businessscore.entity;


import com.springboot.Auditable.Auditable;
import com.springboot.business.entity.Business;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "SCORE")
@NoArgsConstructor
public class Score extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long scoreId;

    @Column(nullable = false, length = 20)
    private String businessScoreTitle;

    @Column(nullable = false, length = 20)
    private String observerName;

    private int scoreList1 = 1;

    private int scoreList2 = 1;

    private int ScoreList3 = 1;

    private int ScoreList4 = 1;

    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(name = "BUSINESS_ID")
    private Business business;

    public void addBusiness(Business business) {
        this.business = business;
        if(!this.business.getScores().contains(this)){
            this.business.getScores().add(this);
        }
    }

}

