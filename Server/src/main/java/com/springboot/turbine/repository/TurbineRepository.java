package com.springboot.turbine.repository;

import com.springboot.turbine.entity.Turbine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TurbineRepository extends JpaRepository<Turbine, Long> {
    Optional<Turbine> findById(Long turbineId);
}
