package com.springboot.business.mapper;

import com.springboot.business.dto.BusinessPostDto;
import com.springboot.business.dto.BusinessResponseDto;
import com.springboot.business.entity.Business;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BusinessMapper {
    Business businessPostDtoToBusiness(BusinessPostDto businessPostDto);


    default BusinessResponseDto businessToBusinessResponseDto(Business business){
        return new BusinessResponseDto(
                business.getBusinessesId(),
                business.getBusinessTitle()
        );
    }

    default List<BusinessResponseDto> businessToBusinessResponseDtos(List<Business> businessList){
        return businessList.stream()
                .map(business -> {
                    return new BusinessResponseDto(
                            business.getBusinessesId(),
                            business.getBusinessTitle());
                }).collect(Collectors.toList());
    }
}
