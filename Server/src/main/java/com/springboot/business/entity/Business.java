package com.springboot.business.entity;

import com.springboot.Auditable.Auditable;
import com.springboot.businessscore.entity.Score;
import com.springboot.location.entity.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Business extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long BusinessesId;

    @Column(nullable = false, length = 100)
    private String businessTitle;


    // 비지니스 수정과 삭제 갱신 등등 전부 다 로케인션과 정보를 건드는거
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<Location> locations = new ArrayList<>();

    public void addLocations(Location location){
        this.locations.add(location);
        if (location.getBusiness() != this){
            location.addBusiness(this);
        }
    }

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<Score> scores = new ArrayList<>();

    public void addBusinessScores(Score score) {
        this.scores.add(score);
        if (score.getBusiness() != this){
            score.addBusiness(this);
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

