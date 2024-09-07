package com.springboot.turbines.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@Getter
@Setter
@Entity
public class Turbine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long turbinesId;

    @Column(nullable = false, length = 20)
    private String turbineModel;


}
