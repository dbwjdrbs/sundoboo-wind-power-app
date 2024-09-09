package com.springboot.businessscore.repository;

import com.springboot.businessscore.entity.BusinessScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessScoreRepository extends JpaRepository<BusinessScore, Long> {

    Page<BusinessScore>findByBusiness_BusinessesId(long businessesId, Pageable pageable);
}
