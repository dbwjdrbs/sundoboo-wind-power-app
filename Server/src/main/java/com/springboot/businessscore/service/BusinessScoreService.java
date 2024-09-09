package com.springboot.businessscore.service;


import com.springboot.business.entity.Business;
import com.springboot.business.service.BusinessService;
import com.springboot.businessscore.entity.BusinessScore;
import com.springboot.businessscore.repository.BusinessScoreRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BusinessScoreService {
    private final BusinessService businessService;
    private final BusinessScoreRepository businessScoreRepository;

    public BusinessScoreService(BusinessService businessService, BusinessScoreRepository businessScoreRepository) {
        this.businessService = businessService;
        this.businessScoreRepository = businessScoreRepository;
    }

    public BusinessScore postScore(BusinessScore businessScore){
        // Business가 null인지 체크
        if (businessScore.getBusiness() == null ||  businessScore.getBusiness().getBusinessesId() == 0) {
            throw new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND); // 적절한 예외를 발생시킴
        }
        Business findBusiness = businessService.findverifyExistBusiness(businessScore.getBusiness().getBusinessesId());
       businessScore.addBusiness(findBusiness);
       return businessScoreRepository.save(businessScore);
    }

    // 빈데이터 응답 처리기 때문에 없는 아이디 입력하면 빈화면 나와서 따로 예외 안던짐
    public Page<BusinessScore> findScore(long businessesId, int page, int size){
     return businessScoreRepository.findByBusiness_BusinessesId(businessesId, PageRequest.of(page, size, Sort.by("createdAt").descending()));

    }
}
