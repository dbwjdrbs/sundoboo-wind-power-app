package com.springboot.businessscore.repository;

import com.springboot.businessscore.entity.BusinessScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessScoreRepository extends JpaRepository<BusinessScore,Long> {
}
