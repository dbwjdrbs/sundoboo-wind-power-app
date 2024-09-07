package com.springboot.business.controller;

import com.springboot.business.dto.BusinessPostDto;
import com.springboot.business.entity.Business;
import com.springboot.business.mapper.BusinessMapper;
import com.springboot.business.service.BusinessService;
import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
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

@RestController
@RequestMapping("/businesses")
@Validated
@Slf4j
public class BusinessController {

    public static final String BUSINESS_DEFUALT_URL = "/businesses";

    private final BusinessService businessService;
    private final BusinessMapper businessMapper;

    public BusinessController(BusinessService businessService, BusinessMapper businessMapper) {
        this.businessService = businessService;
        this.businessMapper = businessMapper;
    }


    @PostMapping("/registration")
    public ResponseEntity postBusiness(@Valid @RequestBody BusinessPostDto businessPost){

        Business postBusiness = businessService.createdBusiness(businessMapper.businessPostDtoToBusiness(businessPost));
        URI location = UriCreator.creatorUrl(BUSINESS_DEFUALT_URL, postBusiness.getBusinessesId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{business-id}")
    public ResponseEntity getBusiness(@PathVariable("business-id") @Positive long businessId){
        Business getBusiness = businessService.findverifyExistBusiness(businessId);
        return new ResponseEntity(new SingleResponseDto<>(businessMapper.businessToBusinessResponseDto(getBusiness)), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity getBusinesses(@Positive @RequestParam int page,
                                      @Positive @RequestParam int size,
                                        @RequestParam String direction){
        Page<Business> businessPage = businessService.getBusinesses( page -1, size, direction);
        return new ResponseEntity(
                new MultiResponseDto<>(
                        businessMapper.businessToBusinessResponseDtos(businessPage.getContent()), businessPage), HttpStatus.OK);
    }
}
