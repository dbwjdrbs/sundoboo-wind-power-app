package com.springboot.business.entity;


import com.springboot.auditable.Auditable;
import com.springboot.businessscore.entity.BusinessScore;
import com.springboot.location.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Business extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long businessId;

    @Column(nullable = false, length = 20)
    private String businessTitle;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<Location> locations = new ArrayList<>();

    public void addLocations(Location location){
        this.locations.add(location);
        if (location.getBusiness() != this){
            location.addBusiness(this);
        }
    }


    @OneToMany(mappedBy = "business", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<BusinessScore> businessScoreList = new ArrayList<>();

    public void addBusinessScores(BusinessScore businessScore) {
        this.businessScoreList.add(businessScore);
        if (businessScore.getBusiness() != this){
            businessScore.addBusiness(this);
        }
    }
}
