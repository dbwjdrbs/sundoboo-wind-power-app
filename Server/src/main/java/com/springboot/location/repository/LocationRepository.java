package com.springboot.location.repository;

import com.springboot.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByTurbineId(Long turbineId);
}
