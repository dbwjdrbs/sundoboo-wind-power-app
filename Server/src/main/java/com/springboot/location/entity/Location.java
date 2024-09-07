package com.springboot.location.entity;


import com.springboot.business.entity.Businesses;
import com.springboot.turbines.entity.Turbine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long locationId;

    @Column(length = 100)
    private String latitude;

    @Column(length = 100)
    private String longitude;

    @Column(length = 20)
    private String city;

    @Column(length = 20)
    private String island;

    LocalDateTime createdAt = LocalDateTime.now();

    LocalDateTime deletedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "TURBINE_ID")
    private Turbine turbine;


    @ManyToOne
    @JoinColumn(name = "BUSINESS_ID")
    private Businesses businesses;


    public void addBusiness(Businesses businesses){
        this.businesses = businesses;
        if (!businesses.getLocations().contains(this)){
            this.businesses.getLocations().add(this);
        }
    }

}
