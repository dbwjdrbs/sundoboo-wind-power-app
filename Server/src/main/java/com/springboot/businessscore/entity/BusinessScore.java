package com.springboot.businessscore.entity;

import com.springboot.auditable.Auditable;
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
public class BusinessScore extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long businessScoreId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "BUSINESS_ID")
    private Business business;

    @Column(nullable = false, length = 20)
    private String businessScoreTitle;

    @Column
    private int scoreList1;

    @Column
    private int scoreList2;

    @Column
    private int scoreList3;

    @Column
    private int scoreList4;

    @Column(nullable = false, length = 20)
    private String observerName;


    public void addBusiness(Business business) {
        this.business = business;
        if(!this.business.getBusinessScoreList().contains(this)){
            this.business.getBusinessScoreList().add(this);
        }
    }
}