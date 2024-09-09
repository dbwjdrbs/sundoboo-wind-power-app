package com.springboot.businessscore.mapper;


import com.springboot.business.entity.Business;
import com.springboot.businessscore.dto.BusinessScorePostDto;
import com.springboot.businessscore.dto.BusinessScoreResponseDto;
import com.springboot.businessscore.entity.BusinessScore;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BusinessScoreMapper {
    default BusinessScore scorePostDtoToScore(BusinessScorePostDto businessScorePostDto){
        Business business = new Business();
        business.setBusinessesId(businessScorePostDto.getBusinessId());
        BusinessScore businessScore = new BusinessScore();
        businessScore.setBusiness(business);
        businessScore.setBusinessScoreTitle(businessScorePostDto.getBusinessScoreTitle());
        businessScore.setScoreList1(businessScorePostDto.getScoreList1());
        businessScore.setScoreList2(businessScorePostDto.getScoreList2());
        businessScore.setScoreList3(businessScorePostDto.getScoreList3());
        businessScore.setScoreList4(businessScorePostDto.getScoreList4());
        businessScore.setObserverName(businessScorePostDto.getObserverName());
        businessScore.setModifiedAt(businessScorePostDto.getModifiedAt());

        return businessScore;
    }

    default BusinessScoreResponseDto scoreToScoreResponseDto(BusinessScore businessScore){
        BusinessScoreResponseDto responseDto = new BusinessScoreResponseDto();
        responseDto.setScoreId(businessScore.getScoreId());
        responseDto.setBusinessId(businessScore.getBusiness().getBusinessesId());
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
    default List<BusinessScoreResponseDto> scoreToScoresResponseDto(List<BusinessScore> businessScores){
        return businessScores.stream()
                .map(score -> scoreToScoreResponseDto(score)).collect(Collectors.toList());
    }
}
