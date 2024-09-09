package com.springboot.businessscore.controller;


import com.springboot.businessscore.dto.BusinessScoreDto;

import com.springboot.businessscore.entity.BusinessScore;
import com.springboot.businessscore.mapper.BusinessScoreMapper;
import com.springboot.businessscore.service.BusinessScoreService;
import com.springboot.utils.validator.UriCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/Scores")
@Validated
public class BusinessScoreController {

    private final String DEFAULT_BUSINESS_SCORE_URL = "Scores";
    private final BusinessScoreService businessScoreService;
    private final BusinessScoreMapper businessScoreMapper;

    public BusinessScoreController(BusinessScoreService businessScoreService, BusinessScoreMapper businessScoreMapper) {
        this.businessScoreService = businessScoreService;
        this.businessScoreMapper = businessScoreMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity postScore(@Valid @RequestBody BusinessScoreDto.Post requestBody){
        BusinessScore businessScore = businessScoreMapper.businessScorePostDtoToBusinessScore(requestBody);
        BusinessScore createdBusinessScore = businessScoreService.createBusinessScore(requestBody);
        URI location = UriCreator.createUri(DEFAULT_BUSINESS_SCORE_URL,createdBusinessScore.getBusinessScoreId());
        return ResponseEntity.created(location).build();
    };
}
