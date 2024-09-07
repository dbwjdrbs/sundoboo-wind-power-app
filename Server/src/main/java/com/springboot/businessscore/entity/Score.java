package com.springboot.businessscore.entity;


import com.springboot.Auditable.Auditable;
import com.springboot.business.entity.Business;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "BUSINESS_SCORE")
@NoArgsConstructor
public class Score extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long ScoreId;

    @Column(nullable = false, length = 20)
    private String businessScoreTitle;

    @Column(nullable = false, length = 20)
    private String observerName;

    private int scoreList1 = 1;

    private int scoreList2 = 1;

    private int ScoreList3 = 1;

    private int ScoreList4 = 1;

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

