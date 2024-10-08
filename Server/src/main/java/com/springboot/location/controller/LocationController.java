package com.springboot.location.controller;

import com.springboot.business.entity.Business;
import com.springboot.business.service.BusinessService;
import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.location.dto.LocationDto;
import com.springboot.location.entity.Location;
import com.springboot.location.mapper.LocationMapper;
import com.springboot.location.service.LocationService;
import com.springboot.utils.validator.UriCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/locations")
@Validated
@Slf4j
public class LocationController {

    public static final String SCORE_DEFAULT_URL = "/locations";

    private final BusinessService businessService;
    private final LocationService locationService;
    private final LocationMapper locationMapper;

    public LocationController(BusinessService businessService, LocationService locationService, LocationMapper locationMapper) {
        this.businessService = businessService;
        this.locationService = locationService;
        this.locationMapper = locationMapper;
    }

    @PostMapping()
    public ResponseEntity postLocation(@Valid @RequestBody LocationDto.Post requestBody){
        Business business = businessService.verifyExistBusiness(requestBody.getBusinessId());
        Location location = locationMapper.locationPostDtoToLocation(requestBody);
        location.setBusiness(business);
        Location savedLocation = locationService.postLocation(location);
        URI uri = UriCreator.createUri(SCORE_DEFAULT_URL, savedLocation.getLocationId());

        return ResponseEntity.created(uri).build();
    }

    @PatchMapping()
    public ResponseEntity<Void> patch(@Valid @RequestBody LocationDto.Patch requestBody){

       locationService.patchLocation(locationMapper.locationPatchDtoToLocation(requestBody));

        return ResponseEntity.ok().build();

    }
    // 비지니스 아이디가 있어야지 조회할 수 있으니까 걍 PathVariable로 api변경
    @GetMapping()
    public ResponseEntity getLocations(@Positive @RequestParam int page,
                                      @Positive @RequestParam int size){
        //Page<Location> 객체는 해당 페이지에 있는 모든 Score 객체를 포함
        Page<Location> pageLocation = locationService.findLocations(page-1, size);
        // getContent()는 현재 페이지에 포함된 여러 개의 Location 객체를 리스트 형태로 반환해서 Location 객체들의 리스트임
        List<Location> locationList = pageLocation.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto(locationMapper.locationToLocationsResponseDto(locationList), pageLocation),
                HttpStatus.OK);
    }

    @GetMapping("/search/{business-id}")
    public ResponseEntity getLocation(@PathVariable("business-id") @Positive long businessId,
                                      @Positive @RequestParam int page,
                                      @Positive @RequestParam int size) {


        Page<Location> pageLocation = locationService.findLocation(businessId, page-1, size);
        List<Location> locationList = pageLocation.getContent();
        return new ResponseEntity(
                new MultiResponseDto(locationMapper.locationToLocationsResponseDto(locationList), pageLocation),
                HttpStatus.OK);

    }

    @GetMapping("/search/dd")
    public ResponseEntity getDDLocation(@RequestParam("latitude") String latitude,
                                        @RequestParam("longitude") String longitude) {
        Location location = locationService.getDD(latitude, longitude);
        return new ResponseEntity<>(
                new SingleResponseDto<>(locationMapper.locationToDdResponseDto(location)),
                HttpStatus.OK);
    }


    @DeleteMapping("/business/{business-id}")
    public ResponseEntity<Void> deleteLocationsByBusinessId(@PathVariable("business-id") long businessId) {
        locationService.deleteAllLocationsByBusinessId(businessId);
        return ResponseEntity.noContent().build();
    }

}
