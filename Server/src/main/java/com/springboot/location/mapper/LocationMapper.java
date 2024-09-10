package com.springboot.location.mapper;

import com.springboot.business.entity.Business;
import com.springboot.location.dto.LocationDto;
import com.springboot.location.entity.Location;
import com.springboot.turbine.entity.Turbine;
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
    default Location locationPostDtoToLocation(LocationDto.Post requestBody) {
        Business business = new Business();
        business.setBusinessId(requestBody.getBusinessId());
        business.setBusinessTitle(requestBody.getBusinessTitle());

        Turbine turbine = new Turbine();
        turbine.setModelName(requestBody.getModelName());
        turbine.setTurbineId(requestBody.getTurbineId());

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


    default Location locationPatchDtoToLocation(LocationDto.Patch requestBody) {
        Business business = new Business();
        business.setBusinessId(requestBody.getBusinessId());
        //비지니스 타이틀은 여기서 변경 안하게 맵핑 안시킴

        Location location = new Location();
        location.setLocationId(requestBody.getLocationId());
        location.setLatitude(requestBody.getLatitude());
        location.setLongitude(requestBody.getLongitude());
        location.setCity(requestBody.getCity());
        location.setIsland(requestBody.getIsland());
        location.setBusiness(business);

        Turbine turbine = new Turbine();
        turbine.setModelName(requestBody.getModelName());
        turbine.setTurbineId(requestBody.getTurbineId());
        location.setTurbine(turbine);

        return location;
    }

    default LocationDto.Response locationDtoToLocationResponseDto(Location location) {
        // 비즈니스와 터빈의 null 체크를 통해 안전하게 값 설정
        long businessId = (location.getBusiness() != null) ? location.getBusiness().getBusinessId() : 0;
        long turbineId = (location.getTurbine() != null) ? location.getTurbine().getTurbineId() : 0;

        LocationDto.Response responseDto = new LocationDto.Response(
                businessId,                               // 비즈니스 ID
                turbineId,                               // 터빈 ID
                location.getLocationId(),                // 위치 ID
                (location.getBusiness() != null) ? location.getBusiness().getBusinessTitle() : null, // 비즈니스 제목
                (location.getTurbine() != null) ? location.getTurbine().getModelName() : null,       // 모델 이름
                location.getLatitude(),                  // 위도
                location.getLongitude(),                 // 경도
                location.getCity(),                      // 도시
                location.getIsland(),                     // 섬
                location.getDeletedAt()
        );

        return responseDto; // responseDto 반환
    }

    default List<LocationDto.Response> locationToLocationsResponseDto(List<Location> locations) {
        return locations.stream()
                .map(location -> locationDtoToLocationResponseDto(location)).collect(Collectors.toList());
    }
}
