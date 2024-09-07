package com.springboot.business.entity;

import com.springboot.businessscore.entity.BusinessScore;
import com.springboot.location.entity.Location;
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
    @Column(nullable = false)
    private long BusinessesId;

    @Column(nullable = false, length = 100)
    private String businessTitle;

    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime deletedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "businesses", cascade = CascadeType.ALL)
    private List<Location> locations = new ArrayList<>();

    public void addLocations(Location location){
        this.locations.add(location);
        if (location.getBusinesses() != this){
            location.addBusiness(this);
        }
    }

    @OneToMany(mappedBy = "businesses", cascade = CascadeType.ALL)
    private List<BusinessScore> businessScores = new ArrayList<>();

    public void addBusinessScores(BusinessScore businessScore) {
        this.businessScores.add(businessScore);
        if (businessScore.getBusinesses() != this){
            businessScore.addBusiness(this);
        }
    }


    // 비지니스에서 직접적으로 이을 필요 없이 비즈니스랑 로케이션만 이어서 로케이션 통해 조회
//    @OneToMany(mappedBy = "businesses", cascade = CascadeType.ALL)
//    private List<Location> locations = new ArrayList<>();
//
//    public void addTurbines(Location location){
//        this.locations.add(location);
//        if (location.getBusinesses() != this){
//            location.addBusiness(this);
//        }

    }

