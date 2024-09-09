package com.springboot.turbines.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TURBINE")
public class Turbine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long turbineId;

    @Column(name = "model_name", nullable = false, length = 50)
    private String modelName;

    @Column(name = "rated_power", nullable = false)
    private int ratedPower;

    @Column(name = "class_type", nullable = false, length = 10)
    private String classType;

    @Column(name = "cut_in_wind_speed", nullable = false)
    private BigDecimal cutInWindSpeed; // BigDecimal로 소수점 표현

    @Column(name = "rated_wind_speed", nullable = false)
    private BigDecimal ratedWindSpeed; // BigDecimal로 소수점 표현

    @Column(name = "cut_out_wind_speed", nullable = false)
    private BigDecimal cutOutWindSpeed; // BigDecimal로 소수점 표현

    @Column(name = "rotor_diameter", nullable = false)
    private BigDecimal rotorDiameter; // BigDecimal로 소수점 표현

    @Column(name = "extreme_survival_wind_speed", nullable = false)
    private BigDecimal extremeSurvivalWindSpeed; // BigDecimal로 소수점 표현

    @Column(name = "operational_status", nullable = false, length = 20)
    private String operationalStatus;
}