package com.springboot.businessscore.controller;


import com.springboot.business.entity.Business;
import com.springboot.business.service.BusinessService;
import com.springboot.businessscore.dto.ScorePostDto;
import com.springboot.businessscore.entity.Score;
import com.springboot.businessscore.mapper.ScoreMapper;
import com.springboot.businessscore.service.ScoreService;
import com.springboot.utils.validator.UriCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/scores")
@Validated
@Slf4j
public class ScoreController {

    public static final String SCORE_DEFAULT_URL = "/scores";

    private final BusinessService businessService;
    private final ScoreService scoreService;
    private final ScoreMapper scoreMapper;

    public ScoreController(BusinessService businessService, ScoreService scoreService, ScoreMapper scoreMapper) {
        this.businessService = businessService;
        this.scoreService = scoreService;
        this.scoreMapper = scoreMapper;
    }


    // 만약 작동 안되면 비즈니스 아이디 넣기
    @PostMapping("/registration")
    public ResponseEntity postScore(@Valid @RequestBody ScorePostDto scorePostDto){
        // 비즈니스 ID로 비즈니스 객체 조회
        Business business = businessService.findverifyExistBusiness(scorePostDto.getBusinessId());

        // DTO를 엔티티로 변환하고 비즈니스 설정
        Score score = scoreMapper.scorePostDtoToScore(scorePostDto);
        score.setBusiness(business); // 비즈니스 설정

        // Score 저장
        Score savedScore = scoreService.postScore(score);
        URI location = UriCreator.creatorUrl(SCORE_DEFAULT_URL, score.getScoreId());

        return ResponseEntity.created(location).build();
    }
}
