package com.springboot.business.dto;

import com.springboot.businessscore.dto.BusinessScoreResponseDto;
import com.springboot.location.dto.LocationResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class

BusinessResponseDto {
    private long businessId;
    private String businessName;
    private List<LocationResponseDto> locationResponseDtoList;
    private List<BusinessScoreResponseDto> businessScoreResponseDtos;

}
