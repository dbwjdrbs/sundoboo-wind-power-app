package com.springboot.business.controller;

import com.springboot.business.dto.BusinessDto;
import com.springboot.business.entity.Business;
import com.springboot.business.mapper.BusinessMapper;
import com.springboot.business.service.BusinessService;
import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.utils.validator.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/businesses")
@Validated
public class BusinessController {

    private final static String DEFAULT_BUSINESS_URL = "/businesses";
    private final BusinessMapper businessMapper;
    private final BusinessService businessService;

    public BusinessController(BusinessMapper businessMapper, BusinessService businessService) {
        this.businessMapper = businessMapper;
        this.businessService = businessService;
    }

    @PostMapping("/registration")
    public ResponseEntity createBusiness(@RequestBody BusinessDto.Post requestBody){
        Business business = businessMapper.businessPostDtoToBusiness(requestBody);
        Business createdBusiness = businessService.createBusiness(business);
        URI location = UriCreator.createUri(DEFAULT_BUSINESS_URL,createdBusiness.getBusinessId());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{business-id}")
    public ResponseEntity deleteBusiness(@Positive @PathVariable("business-id") long businessId){
        businessService.deleteBusiness(businessId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{business-id}")
    public ResponseEntity getBusiness(@PathVariable("business-id") @Positive long businessId){
        Business getBusiness = businessService.verifyExistBusiness(businessId);
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
