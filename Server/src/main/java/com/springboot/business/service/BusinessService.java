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

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BusinessService {
    private final BusinessRepository businessRepository;

    public BusinessService(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    public Business createBusiness(Business business) {
        verifyBusinessTitle(business.getBusinessTitle());
        return businessRepository.save(business);
    }

    public void deleteBusiness(long businessId) {
        Business findBusiness = verifyExistBusiness(businessId);
        findBusiness.setDeletedAt(LocalDateTime.now());
        businessRepository.save(findBusiness);
    }


    //    사업 생성 시 Title을 통한 검증메서드 있다면 존재 커스텀 예외코드 던짐
    public void verifyBusinessTitle(String businessTitle) {
        Optional<Business> optionalBusiness = businessRepository.findByBusinessTitle(businessTitle);
        optionalBusiness.ifPresent(business -> {
            throw new BusinessLogicException(ExceptionCode.BUSINESS_ALREADY_EXISTS);
        });
    }


    //    검증메서드, 삭제내역, 아이디가 없을 시 예외 처리
    public Business verifyExistBusiness(long businessId) {
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        Business findByBusinessId =
                optionalBusiness.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND));
        if (findByBusinessId.getDeletedAt() != null) {
            new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND);
        }
        return findByBusinessId;
    }

    public Business findverifyExistBusiness(long businessId) {
        // 객체로 받아야지만 널포인트 안터지는거 명심! 컨트롤러에 객체로 전달해주는 로직임
        Optional<Business> business = businessRepository.findById(businessId);
        return business.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND));
    }

    public Page<Business> getBusinesses(int page, int size, String direction) {
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
