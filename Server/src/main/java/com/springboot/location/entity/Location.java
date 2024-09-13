package com.springboot.location.entity;


import com.springboot.auditable.Auditable;
import com.springboot.business.entity.Business;
import com.springboot.turbine.entitiy.Turbine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Location extends Auditable {

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


    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(name = "TURBINE_ID")
    private Turbine turbine;

    @ManyToOne
    @JoinColumn(name = "BUSINESS_ID")
    private Business business;


    public void addBusiness(Business business){
        this.business = business;
        if (!business.getLocations().contains(this)){
            this.business.getLocations().add(this);
        }
    }

}
