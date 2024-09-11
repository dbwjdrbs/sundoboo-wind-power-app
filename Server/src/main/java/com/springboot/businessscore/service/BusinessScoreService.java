package com.springboot.businessscore.service;

import com.springboot.business.entity.Business;
import com.springboot.business.service.BusinessService;
import com.springboot.businessscore.dto.BusinessScoreDto;
import com.springboot.businessscore.entity.BusinessScore;
import com.springboot.businessscore.repository.BusinessScoreRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class BusinessScoreService {

    private final BusinessService businessService;
    private final BusinessScoreRepository businessScoreRepository;

    public BusinessScoreService(BusinessService businessService, BusinessScoreRepository businessScoreRepository) {
        this.businessService = businessService;
        this.businessScoreRepository = businessScoreRepository;
    }

    public BusinessScore createBusinessScore(BusinessScoreDto.Post requestBody) {
        // 검증 로직 추가 가능
        Business findBusiness = businessService.verifyExistBusiness(requestBody.getBusinessId());
        verifyBusinessScoreTitle(requestBody.getBusinessScoreTitle());

        BusinessScore businessScore = new BusinessScore();
        businessScore.setBusiness(findBusiness);
        businessScore.setBusinessScoreTitle(requestBody.getBusinessScoreTitle());
        businessScore.setScoreList1(requestBody.getScoreList1());
        businessScore.setScoreList2(requestBody.getScoreList2());
        businessScore.setScoreList3(requestBody.getScoreList3());
        businessScore.setScoreList4(requestBody.getScoreList4());
        businessScore.setObserverName(requestBody.getObserverName());



        return businessScoreRepository.save(businessScore);

    }

//    사업에 대한 평가를 관찰자 명으로 한정할 경우.
//    public void verifyObServerName(String observerName){
//        Optional<BusinessScore> optionalBusinessScore = businessScoreRepository.findByObserverName(observerName);
//        optionalBusinessScore.ifPresent(BusinessScore -> {
//            throw new BusinessLogicException(ExceptionCode.OBSERVER_ALREADY_EVALUATION);
//        });
//    }

//  사업에 대한 평가를 평가명으로 한정할 경우.
    public void verifyBusinessScoreTitle(String businessScoreTitle){
        Optional<BusinessScore> optionalBusinessScore = businessScoreRepository.findByBusinessScoreTitle(businessScoreTitle);
        optionalBusinessScore.ifPresent(BusinessScore -> {
            throw new BusinessLogicException(ExceptionCode.BUSINESS_SCORE_TITLE_ALREADY_EXISTS);
        });
    }

    // 빈데이터 응답 처리기 때문에 없는 아이디 입력하면 빈화면 나와서 따로 예외 안던짐
    public Page<BusinessScore> findScore(long businessId, int page, int size){
        return businessScoreRepository.findByBusiness_BusinessId(businessId, PageRequest.of(page, size, Sort.by("createdAt").descending()));

    }
}
