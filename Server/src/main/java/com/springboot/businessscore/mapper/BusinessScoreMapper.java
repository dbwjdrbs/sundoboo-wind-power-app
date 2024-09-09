package com.springboot.businessscore.mapper;

import com.springboot.business.entity.Business;
import com.springboot.businessscore.dto.BusinessScoreDto;
import com.springboot.businessscore.entity.BusinessScore;
import org.hibernate.annotations.Source;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BusinessScoreMapper {

    @Mapping(source = "businessId", target = "business.businessId")
    BusinessScore businessScorePostDtoToBusinessScore(BusinessScoreDto.Post requestBody);

    default BusinessScore businessScorePostDtoToScore(BusinessScoreDto.Post requestBody) {
        Business business = new Business();
        business.setBusinessId(requestBody.getBusinessId());
        BusinessScore businessScore = new BusinessScore();
        businessScore.setBusiness(business);
        businessScore.setBusinessScoreTitle(requestBody.getBusinessScoreTitle());
        businessScore.setScoreList1(requestBody.getScoreList1());
        businessScore.setScoreList2(requestBody.getScoreList2());
        businessScore.setScoreList3(requestBody.getScoreList3());
        businessScore.setScoreList4(requestBody.getScoreList4());
        businessScore.setObserverName(requestBody.getObserverName());


        return businessScore;
    }

    default BusinessScoreDto.Response businessScoreToBusinessScoreResponseDto(BusinessScore businessScore) {
        BusinessScoreDto.Response responseDto = new BusinessScoreDto.Response();
        responseDto.setScoreId(businessScore.getBusinessScoreId());
        responseDto.setBusinessId(businessScore.getBusiness().getBusinessId());
        responseDto.setBusinessScoreTitle(businessScore.getBusinessScoreTitle());
        responseDto.setScoreList1(businessScore.getScoreList1());
        responseDto.setScoreList2(businessScore.getScoreList2());
        responseDto.setScoreList3(businessScore.getScoreList3());
        responseDto.setScoreList4(businessScore.getScoreList4());
        responseDto.setObserverName(businessScore.getObserverName());
        responseDto.setCreatedAt(businessScore.getCreatedAt());
        responseDto.setModifiedAt(businessScore.getModifiedAt());
        return responseDto;
    }

    // 여러 score를 포함한 리스트이기 때문에 scores의 모든 요소(여기서는 각 score객체)를 다 한꺼번에 디티오로 변환하는 과정
    default List<BusinessScoreDto.Response> businessScoresToBusinessScoreResponseDtos(List<BusinessScore> businessScores) {
        return businessScores.stream()
                .map(this::businessScoreToBusinessScoreResponseDto) // 단일 점수 객체를 매핑하는 메서드 호출
                .collect(Collectors.toList());
    }
}

