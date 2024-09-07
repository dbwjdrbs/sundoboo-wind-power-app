package com.springboot.location.mapper;

import com.springboot.location.dto.LocationDto;
import com.springboot.location.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {
    default LocationDto.Response turbineToResponseDto(Location location){
        LocationDto.Response response = new LocationDto.Response();
        response.setTurbineId(location.getTurbineId());
        response.setKorName(location.getKorName());
        response.setEngName(location.getEngName());
        response.setLatitude(location.getLatitude());
        response.setLongitude(location.getLongitude());
        response.setCreatedAt(location.getCreatedAt());

        return response;
    }
}
