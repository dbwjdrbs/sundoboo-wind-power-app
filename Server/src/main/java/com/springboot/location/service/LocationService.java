package com.springboot.location.service;

import com.springboot.business.entity.Business;
import com.springboot.business.service.BusinessService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.location.entity.Location;
import com.springboot.location.repository.LocationRepository;
import com.springboot.turbines.entity.Turbine;
import com.springboot.turbines.repository.TurbineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class LocationService {

    private final BusinessService businessService;
    private final LocationRepository locationRepository;
    private final TurbineRepository turbineRepository;

    @Autowired
    public LocationService(BusinessService businessService, LocationRepository locationRepository, TurbineRepository turbineRepository) {
        this.businessService = businessService;
        this.locationRepository = locationRepository;
        this.turbineRepository = turbineRepository;
    }

    public Location postLocation(Location location){

        // Business가 null인지 체크하고 예외 던지는 로직
        if (location.getBusiness() == null ||  location.getBusiness().getBusinessesId() == 0) {
            throw new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND); // 적절한 예외를 발생시킴
        }
        // Turbine이 null인지 체크하고 예외 던지는 로직
        if (location.getTurbine() == null || location.getTurbine().getTurbineId() <= 0) {
            throw new BusinessLogicException(ExceptionCode.TURBINE_NOT_FOUND);
        }

        // Turbine이 실제 데이터베이스에 존재하는지 확인
        Turbine findTurbine = turbineRepository.findById(location.getTurbine().getTurbineId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.TURBINE_NOT_FOUND));

        location.setTurbine(findTurbine); // 실제 Turbine 설정

        // 널에 안걸렸다면 찾은 비지니스 아이디를 findBusiness 할당
        Business findBusiness = businessService.findverifyExistBusiness(location.getBusiness().getBusinessesId());
        location.addBusiness(findBusiness);
        return locationRepository.save(location);
    }

    public Location patchLocation(Location location){


        Location findLocation = findVerifyExistLocation(location.getLocationId());
        // 비지니스아디 검증하는 로직 필요할까? 쳐피 로케이션 한번 누르고 이후 재검증할 때 하는건디.. 우선 주석 처리
//        Business business = businessService.findverifyExistBusiness(location.getBusiness().getBusinessesId());
//        location.setBusiness(business);

        // Turbine ID가 유효한 경우에만 조회
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
        Optional<Location> location = locationRepository.findById(locationId);
        return location.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LOCATION_NOT_FOUND));
    }
    public Page<Location> findLocation(long businessId, int page, int size){


        return locationRepository.findByBusiness_BusinessesId(businessId, PageRequest.of(page, size, Sort.by("createdAt").descending()));

    }


    public Page<Location> findLocations(int page, int size){
        return locationRepository.findAll( PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }


}
