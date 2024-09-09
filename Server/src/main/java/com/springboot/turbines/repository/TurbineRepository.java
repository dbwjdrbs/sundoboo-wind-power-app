package com.springboot.turbines.repository;

import com.springboot.turbines.entity.Turbine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TurbineRepository extends JpaRepository<Turbine, Long> {
    Optional<Turbine> findById(Long turbineId);
}
