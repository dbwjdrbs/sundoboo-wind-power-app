package com.springboot.businessscore.mapper;

import com.springboot.businessscore.dto.BusinessScoreDto;
import com.springboot.businessscore.entity.BusinessScore;
import org.hibernate.annotations.Source;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BusinessScoreMapper {

    @Mapping(source = "businessId", target = "business.businessId")
    BusinessScore businessScorePostDtoToBusinessScore(BusinessScoreDto.Post requestBody);
}
