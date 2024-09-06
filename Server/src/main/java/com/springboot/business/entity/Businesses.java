package com.springboot.business.entity;

import com.springboot.businessscore.entity.BusinessScore;
import com.springboot.turbines.entity.Turbine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Businesses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Businesses;

    @Column(nullable = false, length = 100)
    private String businessName;

    @Column(nullable = false)
    LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "businesses", cascade = CascadeType.ALL)
    private List<BusinessScore> businessScores = new ArrayList<>();

    public void addBusinessScores(BusinessScore businessScore) {
        this.businessScores.add(businessScore);
        if (businessScore.getBusinesses() != this){
            businessScore.addBusiness(this);
        }
    }

    @OneToMany(mappedBy = "businesses", cascade = CascadeType.ALL)
    private List<Turbine> turbines = new ArrayList<>();

    public void addTurbines(Turbine turbine){
        this.turbines.add(turbine);
        if (turbine.getBusinesses() != this){
            turbine.addBusiness(this);
        }

    }
}
