package com.springboot.turbines.entity;


import com.springboot.business.entity.Businesses;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Turbine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long turbine;

    @Column(length = 100)
    private String korName;

    @Column(length = 100)
    private String engName;

    @Column(nullable = false, length = 100)
    private String latitude;

    @Column(nullable = false, length = 100)
    private String longitude;

    @Column(nullable = false)
    LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    LocalDateTime deletedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "Business_ID")
    private Businesses businesses;


    public void addBusiness(Businesses businesses){
        this.businesses = businesses;
        if (!businesses.getTurbines().contains(this)){
            this.businesses.getTurbines().add(this);
        }
    }

}
