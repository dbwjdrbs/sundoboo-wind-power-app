package com.springboot.businessscore.entity;


import com.springboot.business.entity.Businesses;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "BUSINESS_SCORE")
@NoArgsConstructor
public class BusinessScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long businessScore;

    @Column(nullable = false, length = 20)
    private String businessScoreTitle;

    @Column(nullable = false, length = 20)
    private String observerName;

    private int scoreList1 = 1;

    private int scoreList2 = 1;

    private int ScoreList3 = 1;

    private int ScoreList4 = 1;

    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime deletedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "Business_ID")
    private Businesses businesses;

    public void addBusiness(Businesses businesses) {
        this.businesses = businesses;
        if(!businesses.getBusinessScores().contains(this)){
            this.businesses.getBusinessScores().add(this);
        }
    }

}

