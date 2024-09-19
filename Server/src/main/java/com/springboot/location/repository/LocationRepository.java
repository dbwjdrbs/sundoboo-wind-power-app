package com.springboot.location.repository;

import com.springboot.business.entity.Business;
import com.springboot.location.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface LocationRepository extends JpaRepository<Location, Long> {
    // 비지니스 아이디가 있어야지 찾을수 있으니까? 아님 로케아션아디로 찾나 그건 아닌듟
    Page<Location> findAll(Pageable pageable);

//    @Query("SELECT l FROM Location l WHERE l.business.businessId = :businessId")
//    Page<Location> findByBusinessId(Long businessId, Pageable pageable);

//    Optional <Location> findById(long locationId);

    Page<Location> findByBusiness(Business business, Pageable pageable);

    Optional<Location> findByLatitudeAndLongitude(String latitude, String longitude);
}
