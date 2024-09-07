package com.springboot.location.controller;


import com.springboot.dto.SingleResponseDto;
import com.springboot.location.service.LocationService;
import com.springboot.location.entity.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/turbines")
@Validated
@Slf4j
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/{turbine-id}")
    public ResponseEntity getTurbine(@PathVariable("turbine-id") @Positive long turbineId){
        Location getLocation = locationService.findTurbine(turbineId);
        return new ResponseEntity<>(
                new SingleResponseDto<>()
        )

    }
}
