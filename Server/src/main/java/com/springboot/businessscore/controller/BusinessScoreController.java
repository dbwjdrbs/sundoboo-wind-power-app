package com.springboot.businessscore.controller;


import com.springboot.business.entity.Business;
import com.springboot.business.service.BusinessService;
import com.springboot.businessscore.dto.BusinessScoreDto;

import com.springboot.businessscore.entity.BusinessScore;
import com.springboot.businessscore.mapper.BusinessScoreMapper;
import com.springboot.businessscore.service.BusinessScoreService;
import com.springboot.dto.MultiResponseDto;
import com.springboot.utils.validator.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/scores")
@Validated
public class BusinessScoreController {

    private final String DEFAULT_BUSINESS_SCORE_URL = "/scores";
    private final BusinessScoreService businessScoreService;
    private final BusinessScoreMapper businessScoreMapper;
    private final BusinessService businessService;

    public BusinessScoreController(BusinessScoreService businessScoreService, BusinessScoreMapper businessScoreMapper, BusinessService businessService) {
        this.businessScoreService = businessScoreService;
        this.businessScoreMapper = businessScoreMapper;
        this.businessService = businessService;
    }

    @PostMapping("/registration")
    public ResponseEntity postScore(@Valid @RequestBody BusinessScoreDto.Post requestBody){
        BusinessScore businessScore = businessScoreMapper.businessScorePostDtoToBusinessScore(requestBody);
        BusinessScore createdBusinessScore = businessScoreService.createBusinessScore(businessScore);
        URI location = UriCreator.createUri(DEFAULT_BUSINESS_SCORE_URL,createdBusinessScore.getBusinessScoreId());
        return ResponseEntity.created(location).build();
    }


    @GetMapping("/search/{business-id}")
    public ResponseEntity getScores(@PathVariable("business-id") @Positive long businessId,
                                    @Positive @RequestParam int page,
                                    @Positive @RequestParam int size){
        Business findBusiness = businessService.verifyExistBusiness(businessId);
        //Page<Score> 객체는 해당 페이지에 있는 모든 Score 객체를 포함
        Page<BusinessScore> pageScores = businessScoreService.findScore(findBusiness.getBusinessId(), page-1, size);
        // getContent()는 현재 페이지에 포함된 여러 개의 Score 객체를 리스트 형태로 반환해서 Score 객체들의 리스트임
        List<BusinessScore> businessScoreList = pageScores.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto(businessScoreMapper.businessScoresToBusinessScoreResponseDtos(businessScoreList), pageScores),
                HttpStatus.OK);
    }
}
