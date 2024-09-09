package com.springboot.businessscore.controller;


import com.springboot.business.entity.Business;
import com.springboot.business.service.BusinessService;
import com.springboot.businessscore.dto.BusinessScorePostDto;
import com.springboot.businessscore.entity.BusinessScore;
import com.springboot.businessscore.mapper.BusinessScoreMapper;
import com.springboot.businessscore.service.BusinessScoreService;
import com.springboot.dto.MultiResponseDto;
import com.springboot.utils.validator.UriCreator;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class BusinessScoreController {

    public static final String SCORE_DEFAULT_URL = "/scores";

    private final BusinessService businessService;
    private final BusinessScoreService businessScoreService;
    private final BusinessScoreMapper businessScoreMapper;

    public BusinessScoreController(BusinessService businessService, BusinessScoreService businessScoreService, BusinessScoreMapper businessScoreMapper) {
        this.businessService = businessService;
        this.businessScoreService = businessScoreService;
        this.businessScoreMapper = businessScoreMapper;
    }


    // 만약 작동 안되면 비즈니스 아이디 넣기
    @PostMapping("/registration")
    public ResponseEntity postScore(@Valid @RequestBody BusinessScorePostDto businessScorePostDto){
        // 비즈니스 ID로 비즈니스 객체 조회, 객체로 받아서 business할당하는거
        Business business = businessService.findverifyExistBusiness(businessScorePostDto.getBusinessId());

        // DTO를 엔티티로 변환하고 비즈니스 설정
        BusinessScore businessScore = businessScoreMapper.scorePostDtoToScore(businessScorePostDto);

        // 비즈니스 설정 및 scores에 추가
        business.addBusinessScores(businessScore); // 양방향 관계 설정
        // Score 저장
        BusinessScore savedBusinessScore = businessScoreService.postScore(businessScore);
        URI location = UriCreator.creatorUrl(SCORE_DEFAULT_URL, savedBusinessScore.getScoreId());

        return ResponseEntity.created(location).build();
    }

    // 비지니스 아이디가 있어야지 조회할 수 있으니까 걍 PathVariable로 api변경
    @GetMapping()
    public ResponseEntity getScores(@PathVariable("businessId") @Positive long businessId,
                                    @Positive @RequestParam int page,
                                    @Positive @RequestParam int size){
        //Page<Score> 객체는 해당 페이지에 있는 모든 Score 객체를 포함
        Page<BusinessScore> pageScores = businessScoreService.findScore(businessId, page-1, size);
        // getContent()는 현재 페이지에 포함된 여러 개의 Score 객체를 리스트 형태로 반환해서 Score 객체들의 리스트임
        List<BusinessScore> businessScoreList = pageScores.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto(businessScoreMapper.scoreToScoresResponseDto(businessScoreList), pageScores),
                HttpStatus.OK);
    }
}
