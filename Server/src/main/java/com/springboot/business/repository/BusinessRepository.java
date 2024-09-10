package com.springboot.business.repository;

import com.springboot.business.entity.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByBusinessTitle(String businessTitle);
    @Query("SELECT b FROM Business b WHERE b.deletedAt IS NULL")
    Page<Business> findAll(PageRequest pageRequest);
    Page<Business> findByDeletedAtIsNull(PageRequest pageRequest);
}
