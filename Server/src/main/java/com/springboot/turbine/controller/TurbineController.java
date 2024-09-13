package com.springboot.turbine.controller;

import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.turbine.entitiy.Turbine;
import com.springboot.turbine.repository.TurbineRepository;
import com.springboot.turbine.service.TurbineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/turbines")
@Validated
@Slf4j
public class TurbineController {
    public static final String SCORE_DEFAULT_URL = "/turbines";

    private final TurbineRepository turbineRepository;
    private final TurbineService turbineService;

    public TurbineController(TurbineRepository turbineRepository, TurbineService turbineService) {
        this.turbineRepository = turbineRepository;
        this.turbineService = turbineService;
    }

    @GetMapping("/{turbine-id}")
    public ResponseEntity getTurbine(@PathVariable("turbineId") @Positive long turbineId){
        Turbine findTurbine = turbineService.findTurbine(turbineId);
        return new ResponseEntity(new SingleResponseDto<>(findTurbine), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity getTurbines(@Positive @RequestParam int page,
                                      @Positive @RequestParam int size){
        Page<Turbine> turbinePage = turbineService.findTurbines(page-1, size);
        List<Turbine> turbineList = turbinePage.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(turbineList, turbinePage), HttpStatus.OK);
    }
}
