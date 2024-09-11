package com.springboot.business.mapper;

import com.springboot.business.dto.BusinessDto;
import com.springboot.business.entity.Business;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BusinessMapper {
    Business businessPostDtoToBusiness(BusinessDto.Post requestBody);

    BusinessDto.Response businessToBusinessResponseDto(Business business);
}
