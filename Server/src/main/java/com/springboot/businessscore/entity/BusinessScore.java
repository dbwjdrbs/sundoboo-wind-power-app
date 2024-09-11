package com.springboot.businessscore.entity;

import com.springboot.business.entity.Business;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long businessScoreId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "BUSINESS_ID")
    private Business business;

    @Column
    private String businessScoreTitle;

    @Column
    private int scoreList1 = 1;

    @Column
    private int scoreList2 = 1;

    @Column
    private int scoreList3 = 1;

    @Column
    private int scoreList4 = 1;

    @Column
    private String observerName;

}
