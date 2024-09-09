package com.springboot.businessscore.repository;

import com.springboot.businessscore.entity.BusinessScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessScoreRepository extends JpaRepository<BusinessScore,Long> {
    Optional<BusinessScore> findByObserverName(String observerName);
    Optional<BusinessScore> findByBusinessScoreTitle(String businessScoreTitle);
}
