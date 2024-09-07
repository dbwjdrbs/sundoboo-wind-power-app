package com.springboot.business.repository;

import com.springboot.business.entity.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByBusinessTitle(String businessTitle);

    Optional<Business> findById(Long businessId);


}
