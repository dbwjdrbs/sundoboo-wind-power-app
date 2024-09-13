package com.springboot.location.service;

import com.springboot.business.entity.Business;
import com.springboot.business.repository.BusinessRepository;
import com.springboot.business.service.BusinessService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.location.entity.Location;
import com.springboot.location.repository.LocationRepository;
import com.springboot.turbine.entitiy.Turbine;
import com.springboot.turbine.repository.TurbineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LocationService {

    private final BusinessService businessService;
    private final LocationRepository locationRepository;
    private final TurbineRepository turbineRepository;

    public LocationService(BusinessService businessService, LocationRepository locationRepository, TurbineRepository turbineRepository) {
        this.businessService = businessService;
        this.locationRepository = locationRepository;
        this.turbineRepository = turbineRepository;
    }


    public Location postLocation(Location location){

        // Business가 null인지 체크하고 예외 던지는 로직

        if (location.getBusiness() == null ||  location.getBusiness().getBusinessId() == 0) {
            throw new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND); // 적절한 예외를 발생시킴
        }
//        // Turbine이 null인지 체크하고 예외 던지는 로직
//        if (location.getTurbine() == null || location.getTurbine().getTurbineId() <= 0) {
//            throw new BusinessLogicException(ExceptionCode.TURBINE_NOT_FOUND);
//        }
//
//        // Turbine이 실제 데이터베이스에 존재하는지 확인
//        Turbine findTurbine = turbineRepository.findById(location.getTurbine().getTurbineId())
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.TURBINE_NOT_FOUND));

//        location.setTurbine(findTurbine); // 실제 Turbine 설정

        // 널에 안걸렸다면 찾은 비지니스 아이디를 findBusiness 할당
        Business findBusiness = businessService.verifyExistBusiness(location.getBusiness().getBusinessId());
        location.setLatitude(location.getLatitude());
        location.setLongitude(location.getLongitude());
        findBusiness.addLocations(location);
        location.addBusiness(findBusiness);

        return locationRepository.save(location);
    }

    public Location patchLocation(Location location){


        Location findLocation = findVerifyExistLocation(location.getLocationId());

        Business business = businessService.verifyExistBusiness(location.getBusiness().getBusinessId());
        findLocation.setBusiness(business);

        if (location.getTurbine() != null && location.getTurbine().getTurbineId() > 0) {
            Turbine turbine = turbineRepository.findById(location.getTurbine().getTurbineId())
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.TURBINE_NOT_FOUND));
            findLocation.setTurbine(turbine);
        } else {
            // Turbine이 없으면 예외 처리
            throw new BusinessLogicException(ExceptionCode.TURBINE_NOT_FOUND);
        }

        Optional.ofNullable(location.getLatitude())
                .ifPresent(latitude -> findLocation.setLatitude(latitude));
        Optional.ofNullable(location.getLongitude())
                .ifPresent(longitude -> findLocation.setLongitude(longitude));
        Optional.ofNullable(location.getCity())
                .ifPresent(city -> findLocation.setCity(city));
        Optional.ofNullable(location.getIsland())
                .ifPresent(island -> findLocation.setIsland(island));
        
        findLocation.setModifiedAt(LocalDateTime.now());
        return locationRepository.save(findLocation);


    }

    public Location findVerifyExistLocation(long locationId){
        Optional<Location> optionalLocation = locationRepository.findById(locationId);

        Location findByLocationId =
                optionalLocation.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LOCATION_NOT_FOUND));

        if (findByLocationId.getDeletedAt() != null){
          throw new BusinessLogicException(ExceptionCode.LOCATION_NOT_FOUND);
        }
        return findByLocationId;
    }

    public void verifyLocation(Location location){
        Location findLocation = findVerifyExistLocation(location.getLocationId());
        String findLatitude = findLocation.getLatitude();
        String findLongitude = findLocation.getLongitude();
        if (location.getLatitude().equals(findLatitude) && location.getLongitude().equals(findLongitude)){
            throw new BusinessLogicException(ExceptionCode.ALREADY_LOCATION_EXISTS);
        }
    }


    public Page<Location> findLocations(int page, int size){
        return locationRepository.findAll( PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }
    // 어쳐피 같은 비지니스 아이디를 받으니까 정렬할 때는 로케이션 아이디 기준으로 정렬
    public Page<Location> findLocation(long businessId, int page, int size){
        Business findBusiness = businessService.verifyExistBusiness(businessId);
        return locationRepository.findByBusiness(findBusiness, PageRequest.of(page, size, Sort.by("locationId").descending()));

    }
    public void deleteAllLocationsByBusinessId(long businessId) {
        // 비즈니스가 존재하는지 확인
        Business findBusiness = businessService.verifyExistBusiness(businessId);

        // 비즈니스에 연결된 모든 Location을 조회
        List<Location> locations = findBusiness.getLocations();

        // 각 Location 삭제
        for (Location location : locations) {
            locationRepository.delete(location);
        }
    }

    // 위치를 삭제하는 기존 메서드
    public void deleteLocation(long locationId) {
        Location findLocation = findVerifyExistLocation(locationId);
        locationRepository.delete(findLocation);
    }

}

