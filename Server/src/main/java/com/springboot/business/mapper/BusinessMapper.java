package com.springboot.business.mapper;

import com.springboot.business.dto.BusinessPostDto;
import com.springboot.business.dto.BusinessResponseDto;
import com.springboot.business.entity.Business;
import com.springboot.businessscore.dto.BusinessScoreResponseDto;
import com.springboot.businessscore.entity.BusinessScore;
import com.springboot.location.dto.LocationResponseDto;
import com.springboot.location.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BusinessMapper {
    Business businessPostDtoToBusiness(BusinessPostDto businessPostDto);

    BusinessScoreResponseDto scoreToScoreResponseDto(BusinessScore businessScore);
    @Mapping(target = "turbineId", source = "turbine.turbineId")
    LocationResponseDto locationToLocationResponseDto(Location location);

    default BusinessResponseDto businessToBusinessResponseDto(Business business) {

        BusinessResponseDto responseDto = new BusinessResponseDto();
        responseDto.setBusinessName(business.getBusinessTitle());
        responseDto.setBusinessId(business.getBusinessesId());
        // 위처럼 넣으면 타입이 안맞아서 리스트 객체로 받는 코드 설정
        List<BusinessScoreResponseDto> businessScoreResponseDtos = business.getBusinessScores().stream()
                .map(this::scoreToScoreResponseDto)
                .collect(Collectors.toList());
        businessScoreResponseDtos.stream()
                        .forEach(score -> {
                            score.setBusinessId(business.getBusinessesId());
                        });
        responseDto.setBusinessScoreResponseDtos(businessScoreResponseDtos);


        // 위처럼 넣으면 타입이 안맞아서 리스트 객체로 받는 코드 설정
        List<LocationResponseDto> locationResponseDtos = business.getLocations().stream()
                .map(this::locationToLocationResponseDto)
                .collect(Collectors.toList());
        locationResponseDtos.stream()
                        .forEach(location -> {
                            location.setBusinessId(business.getBusinessesId());
                        });
        responseDto.setLocationResponseDtoList(locationResponseDtos);

        return  responseDto;
    }

    default List<BusinessResponseDto> businessToBusinessResponseDtos(List<Business> businessList) {
        return businessList.stream()
                .map(business -> businessToBusinessResponseDto(business)).collect(Collectors.toList());
    }
}
