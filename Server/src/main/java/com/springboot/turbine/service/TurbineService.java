package com.springboot.turbine.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.turbine.entitiy.Turbine;
import com.springboot.turbine.repository.TurbineRepository;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Getter
@Transactional
public class TurbineService {
    private final TurbineRepository turbineRepository;

    public TurbineService(TurbineRepository turbineRepository) {
        this.turbineRepository = turbineRepository;
    }

    public Turbine findTurbine(long turbineId){
        Optional<Turbine> optionalTurbine = turbineRepository.findById(turbineId);

        Turbine findByTurbineId =
                optionalTurbine.orElseThrow(() -> new BusinessLogicException(ExceptionCode.TURBINE_NOT_FOUND));
        return findByTurbineId;
    }
    public Page<Turbine> findTurbines(int page, int size){
        return turbineRepository.findAll(PageRequest.of(page, size, Sort.by("turbineId").descending()));
    }
}
