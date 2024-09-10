package com.springboot.businessscore.repository;

import com.springboot.businessscore.entity.BusinessScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BusinessScoreRepository extends JpaRepository<BusinessScore,Long> {
    Optional<BusinessScore> findByObserverName(String observerName);
    Optional<BusinessScore> findByBusinessScoreTitle(String businessScoreTitle);
    Page<BusinessScore> findByBusiness_BusinessId(long businessId, Pageable pageable);
}
