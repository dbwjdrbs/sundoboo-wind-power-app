package com.springboot.businessscore.service;


import com.springboot.business.entity.Business;
import com.springboot.business.service.BusinessService;
import com.springboot.businessscore.entity.Score;
import com.springboot.businessscore.repository.ScoreRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ScoreService {
    private final BusinessService businessService;
    private final ScoreRepository scoreRepository;

    public ScoreService(BusinessService businessService, ScoreRepository scoreRepository) {
        this.businessService = businessService;
        this.scoreRepository = scoreRepository;
    }

    public Score postScore(Score score){
        // Business가 null인지 체크
        if (score.getBusiness() == null ||  score.getBusiness().getBusinessesId() == 0) {
            throw new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND); // 적절한 예외를 발생시킴
        }
        Business findBusiness = businessService.findverifyExistBusiness(score.getBusiness().getBusinessesId());
       score.addBusiness(findBusiness);
       return scoreRepository.save(score);
    }
}
