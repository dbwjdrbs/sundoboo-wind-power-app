package com.springboot.business.mapper;

import com.springboot.business.dto.BusinessDto;
import com.springboot.business.entity.Business;
import com.springboot.businessscore.dto.BusinessScoreDto;
import com.springboot.businessscore.entity.BusinessScore;
import com.springboot.location.dto.LocationDto;
import com.springboot.location.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BusinessMapper {
    Business businessPostDtoToBusiness(BusinessDto.Post requestBody);

    // 매핑이 자동으로 안되는거는 어노테이션으로 따로 ㄱ
    @Mapping( source = "turbine.turbineId", target = "turbineId")
    @Mapping( source = "business.businessId", target = "businessId")
    @Mapping( source = "turbine.modelName", target = "modelName")
    @Mapping( source = "business.businessTitle", target = "businessTitle")
    LocationDto.Response locationToLocationResponseDto(Location location);


    @Mapping(source = "business.businessId", target = "businessId" )
    BusinessScoreDto.Response businessScoreToBusinessScoreResponseDto(BusinessScore businessScore);


    default BusinessDto.Response businessToBusinessResponseDto(Business business) {

        BusinessDto.Response responseDto = new BusinessDto.Response();
        responseDto.setBusinessTitle(business.getBusinessTitle());
        responseDto.setBusinessId(business.getBusinessId());
        responseDto.setCreatedAt(business.getCreatedAt());
        responseDto.setDeletedAt(business.getDeletedAt());
        // 위처럼 넣으면 타입이 안맞아서 리스트 객체로 받는 코드 설정

        List<BusinessScoreDto.Response> businessScoreResponseDtos = business.getBusinessScoreList().stream()
                .map(this::businessScoreToBusinessScoreResponseDto)
                .collect(Collectors.toList());
        businessScoreResponseDtos.stream()
                .forEach(score -> {
                    score.setBusinessId(business.getBusinessId());
                });
        responseDto.setBusinessScores(businessScoreResponseDtos);


        // 위처럼 넣으면 타입이 안맞아서 리스트 객체로 받는 코드 설정
        List<LocationDto.Response> locationResponseDtos = business.getLocations().stream()
                .map(this::locationToLocationResponseDto)
                .collect(Collectors.toList());
        locationResponseDtos.stream()
                .forEach(location -> {
                    location.setBusinessId(business.getBusinessId());
                });
        responseDto.setLocations(locationResponseDtos);

        return  responseDto;
    }

    default List<BusinessDto.Response> businessToBusinessResponseDtos(List<Business> businessList) {
        return businessList.stream()
                .map(business -> businessToBusinessResponseDto(business)).collect(Collectors.toList());
    }
}
