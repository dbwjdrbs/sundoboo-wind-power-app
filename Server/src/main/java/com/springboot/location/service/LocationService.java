package com.springboot.location.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.location.entity.Location;
import com.springboot.location.repository.LocationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

//   @GET("/turbines/{turbine-id}")
//    Call<MappingClass.TurbineResponse> getTurbine(@Path("turbine-id") long turbineId);
//    @GET("/turbines")
//    Call<List<MappingClass.TurbineResponse>> getTurbine();

@Transactional
@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location findTurbine(long turbineId){
        Location findLocation = findverifiedTurbine(turbineId);
        return findverifiedTurbine(findLocation.getTurbineId());
    }


    public Location findverifiedTurbine(long turbineId){
        Optional<Location> optionalTurbine = locationRepository.findByTurbineId(turbineId);
        return optionalTurbine.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND));

    }
}
