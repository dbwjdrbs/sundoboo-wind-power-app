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
    private long businessScore;

    @Column(nullable = false, length = 100)
    private String businessScoreTitle;

    @Column(nullable = false, length = 20)
    private String observerName;

    @Column(nullable = false)
    private int score1;

    @Column(nullable = false)
    private int score2;

    @Column(nullable = false)
    private int Score3;

    @Column(nullable = false)
    private int Score4;

    @Column(nullable = false)
    private LocalDateTime modifiedAt = LocalDateTime.now();

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

