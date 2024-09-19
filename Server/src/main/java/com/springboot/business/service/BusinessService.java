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
        verifyExistBusinessTitle(business.getBusinessTitle());
        return businessRepository.save(business);
    }

    public Business findBusiness(String businessTitle) {
        return verifyBusinessTitle(businessTitle);
    }

    public void deleteBusiness(long businessId) {
        Business findBusiness = verifyExistBusiness(businessId);
        findBusiness.setDeletedAt(LocalDateTime.now());
        businessRepository.save(findBusiness);
    }

    // 사업 생성 시 Title을 통한 검증메서드 있다면 존재 커스텀 예외코드 던짐
    public void verifyExistBusinessTitle(String businessTitle) {
        Optional<Business> optionalBusiness = businessRepository.findByBusinessTitle(businessTitle);
        optionalBusiness.ifPresent(business -> {
            throw new BusinessLogicException(ExceptionCode.BUSINESS_ALREADY_EXISTS);
        });
    }

    // 사업 검색용
    public Business verifyBusinessTitle(String businessTitle) {
        Optional<Business> optionalBusiness = businessRepository.findByBusinessTitle(businessTitle);
        return optionalBusiness.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND));
    }

    // 검증메서드, 삭제내역, 아이디가 없을 시 예외 처리
    public Business verifyExistBusiness(long businessId) {
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);

        Business findByBusinessId = optionalBusiness
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND));

        if (findByBusinessId.getDeletedAt() != null) {
            throw new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND);
        }

        return findByBusinessId;
    }

    // public Business findverifyExistBusiness(long businessId) {
    // // 객체로 받아야지만 널포인트 안터지는거 명심! 컨트롤러에 객체로 전달해주는 로직임
    // Optional<Business> business = businessRepository.findById(businessId);
    //
    // return business.orElseThrow(() -> new
    // BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND));
    // }

    // 검색 기능
    public Page<Business> getBusinesses(int page, int size, String direction, String keyword) {
        PageDirection enumDirection = PageDirection.valueOf(direction);

        Page<Business> businessList;

        if (keyword == null || keyword.isEmpty() || keyword == " ") {
            switch (enumDirection) {
                case PAGE_CREATED_AT_ASC:
                    businessList = businessRepository.findByDeletedAtIsNull(
                            PageRequest.of(page, size, Sort.by("createdAt").ascending()));
                    break;
                case PAGE_CREATED_AT_DESC:
                    businessList = businessRepository.findByDeletedAtIsNull(
                            PageRequest.of(page, size, Sort.by("createdAt").descending()));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid direction parameter: " + direction);
            }
        } else {
            switch (enumDirection) {
                case PAGE_CREATED_AT_ASC:
                    businessList = businessRepository.findByDeletedAtIsNullAndBusinessTitleContaining(
                            PageRequest.of(page, size, Sort.by("createdAt").ascending()), keyword);
                    break;
                case PAGE_CREATED_AT_DESC:
                    businessList = businessRepository.findByDeletedAtIsNullAndBusinessTitleContaining(
                            PageRequest.of(page, size, Sort.by("createdAt").descending()), keyword);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid direction parameter: " + direction);
            }
        }

        return businessList;
    }

}
