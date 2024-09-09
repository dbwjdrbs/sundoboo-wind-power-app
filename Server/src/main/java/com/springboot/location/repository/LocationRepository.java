package com.springboot.location.repository;

import com.springboot.location.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Location, Long> {
    // 비지니스 아이디가 있어야지 찾을수 있으니까? 아님 로케아션아디로 찾나 그건 아닌듟
    Page<Location> findAll(Pageable pageable);

//    Page<Location> findByBusinessId(Long businessId, Pageable pageable);
}
