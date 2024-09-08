package com.springboot.businessscore.repository;

import com.springboot.businessscore.entity.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    Page<Score>findByBusiness_BusinessesId(long businessesId, Pageable pageable);
}
