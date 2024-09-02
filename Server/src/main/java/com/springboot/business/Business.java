package com.springboot.business;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long businessId;

}
