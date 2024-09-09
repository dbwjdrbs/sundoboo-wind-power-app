package com.springboot.location.mapper;

import com.springboot.business.entity.Business;
import com.springboot.location.dto.LocationPatchDto;
import com.springboot.location.dto.LocationPostDto;
import com.springboot.location.dto.LocationResponseDto;
import com.springboot.location.entity.Location;
import com.springboot.turbines.entity.Turbine;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

// MapStruct 라이브러리에서 사용, 객체 간의 매핑을 자동으로 처리해주는 인터페이스에 붙이는 어노테이션
// componentModel = "spring" ->
// 스프링 통합: 이 속성은 MapStruct가 생성한 매퍼 클래스가 Spring의 컴포넌트로 등록되도록 설정
//  unmappedSourcePolicy = ReportingPolicy.IGNORE ->
//  매핑되지 않는 필드 처리: 이 속성은 매핑 메서드에서 소스 객체의 필드가 대상 객체에 매핑되지 않을 경우의 정책을 설정
@Mapper(componentModel = "spring")
public interface LocationMapper {
    default Location locationPostDtoToLocation(LocationPostDto locationPostDto){
        Business business = new Business();
        business.setBusinessesId(locationPostDto.getBusinessId());
        business.setBusinessTitle(locationPostDto.getBusinessTitle());

        Turbine turbine = new Turbine();
        turbine.setModelName(locationPostDto.getModelName());
        turbine.setTurbineId(locationPostDto.getTurbineId());

        Location location = new Location();
        location.setLatitude(location.getLatitude());
        location.setLongitude(location.getLongitude());
        location.setCity(location.getCity());
        location.setIsland(location.getIsland());
        location.setCreatedAt(location.getCreatedAt());
        location.setModifiedAt(location.getModifiedAt());
        location.setBusiness(business);
        location.setTurbine(turbine);


        return location;
    }


    default Location locationPatchDtoToLocation(LocationPatchDto locationPatchDto){

        Location location = new Location();
        location.setLatitude(locationPatchDto.getLatitude());
        location.setLongitude(locationPatchDto.getLongitude());
        location.setCity(locationPatchDto.getCity());
        location.setIsland(locationPatchDto.getIsland());

        Turbine turbine = new Turbine();
        turbine.setTurbineId(locationPatchDto.getTurbineId());
        location.setTurbine(turbine);

        return location;
    }

    default LocationResponseDto locationDtoToLocationResponseDto(Location location){

        LocationResponseDto responseDto = new LocationResponseDto();


        responseDto.setBusinessId(location.getBusiness().getBusinessesId());
        responseDto.setLocationId(location.getLocationId());
        responseDto.setLatitude(location.getLatitude());
        responseDto.setCity(location.getCity());
        responseDto.setIsland(location.getIsland());
        responseDto.setCreatedAt(location.getCreatedAt());
        responseDto.setModifiedAt(location.getModifiedAt());
        responseDto.setTurbineId(location.getTurbine().getTurbineId());
        responseDto.setBusinessTitle(location.getBusiness().getBusinessTitle());
        responseDto.setModelName(location.getTurbine().getModelName());

        return responseDto;
    }
    default List<LocationResponseDto> locationToLocationsResponseDto(List<Location> locations){
        return locations.stream()
                .map(location -> locationDtoToLocationResponseDto(location)).collect(Collectors.toList());
    }
}
