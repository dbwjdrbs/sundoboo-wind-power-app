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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class BusinessScoreService {

    private final BusinessService businessService;
    private final BusinessScoreRepository businessScoreRepository;

    public BusinessScoreService(BusinessService businessService, BusinessScoreRepository businessScoreRepository) {
        this.businessService = businessService;
        this.businessScoreRepository = businessScoreRepository;
    }

    public BusinessScore createBusinessScore(BusinessScore businessScore) {
        // 검증 로직 추가 가능
        Business findBusiness = businessService.verifyExistBusiness(businessScore.getBusiness().getBusinessId());

        BusinessScore businessScore1 = new BusinessScore();
        businessScore1.setBusiness(findBusiness);
        businessScore1.setBusinessScoreTitle(businessScore.getBusinessScoreTitle());
        businessScore1.setScoreList1(businessScore.getScoreList1());
        businessScore1.setScoreList2(businessScore.getScoreList2());
        businessScore1.setScoreList3(businessScore.getScoreList3());
        businessScore1.setScoreList4(businessScore.getScoreList4());
        businessScore1.setObserverName(businessScore.getObserverName());

        return businessScoreRepository.save(businessScore);

    }

//    사업에 대한 평가를 관찰자 명으로 한정할 경우.
//    public void verifyObServerName(String observerName){
//        Optional<BusinessScore> optionalBusinessScore = businessScoreRepository.findByObserverName(observerName);
//        optionalBusinessScore.ifPresent(BusinessScore -> {
//            throw new BusinessLogicException(ExceptionCode.OBSERVER_ALREADY_EVALUATION);
//        });
//    }

    // 빈데이터 응답 처리기 때문에 없는 아이디 입력하면 빈화면 나와서 따로 예외 안던짐
    public Page<BusinessScore> findScore(long businessId, int page, int size){
        return businessScoreRepository.findByBusiness_BusinessId(businessId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }
}
