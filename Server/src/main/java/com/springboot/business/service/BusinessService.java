package com.springboot.business.service;


import com.springboot.business.entity.Business;
import com.springboot.business.repository.BusinessRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.response.PageDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
@Transactional
public class BusinessService {

    private final BusinessRepository businessRepository;

    public BusinessService(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }


    public Business createdBusiness(Business business){
                    verifyExistBusinessId(business.getBusinessTitle());
                    return businessRepository.save(business);
    }

    public void verifyExistBusinessId(String businessTitle){
        Optional<Business> findByBusinessTitle = businessRepository.findByBusinessTitle(businessTitle);
        if (findByBusinessTitle.isPresent()){
            throw new BusinessLogicException(ExceptionCode.BUSINESS_EXISTS);
        }
    }
    public Business findverifyExistBusiness(long businessId){
        // 객체로 받아야지만 널포인트 안터지는거 명심! 컨트롤러에 객체로 전달해주는 로직임
        Optional<Business> business = businessRepository.findById(businessId);
        return business.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND));
    }


    public Page<Business> getBusinesses(int page, int size, String direction){
        PageDirection enumDirection = PageDirection.valueOf(direction);

        Page<Business> businessList;
        switch (enumDirection) {
            case PAGE_CREATEDATASC:
                businessList = businessRepository.findAll(
                        PageRequest.of(page, size, Sort.by("businessTitle").descending()));
                break;
            case PAGE_CREATEDATDESC:
                businessList = businessRepository.findAll(
                        PageRequest.of(page, size, Sort.by("businessTitle").ascending()));
                break;
            default:
                throw new IllegalArgumentException("Invalid direction parameter: " + direction);
        }
        return businessList;
    }
}
