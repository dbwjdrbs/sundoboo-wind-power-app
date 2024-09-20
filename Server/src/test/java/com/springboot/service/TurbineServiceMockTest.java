package com.springboot.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.turbine.entitiy.Turbine;
import com.springboot.turbine.repository.TurbineRepository;
import com.springboot.turbine.service.TurbineService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Transactional
@ExtendWith(MockitoExtension.class)
public class TurbineServiceMockTest {

    @Mock
    private TurbineRepository turbineRepository;

    // 목을 주입하는 대상이 꼭 있어야함! InjectMocks 설정 꼭 확인
    @InjectMocks
    private TurbineService turbineService;


    @DisplayName("findTurbine 성공 테스트")
    @Test
    void findTurbineTest() {
        // given
        // 터빈 아이디 설정
        long turbineId = 1L;
        Turbine turbine = new Turbine();
        turbine.setTurbineId(turbineId);

        // 롱 타입을 받으면 무조건 터빈 반환
        given(turbineRepository.findById(Mockito.anyLong())).willReturn(Optional.of(turbine));

        // when
        // 실제 서비스 메서드를 호출하고 그 결과를 받아옴
        Turbine result = turbineService.findTurbine(turbineId);

        // then
        // 검증 진행 널 아님 확인
        assertNotNull(result);
        // findTurbine 메서드 호출 결과로 반환된 Turbine 객체에서 ID와 테스트에서 사용자가 정의한 값인 ID 비교
        assertEquals(turbineId, result.getTurbineId());
        // 재확인 -> 실제로 해당 메서드가 사용 되었는지
        verify(turbineRepository).findById(turbineId);
    }
    // 실패 원인 분석
    @DisplayName("findTurbines 성공 테스트")
    @Test
    void findTurbinesTest1() {
        // given
        // 터빈 객체 생성해서 리스트로 만듬
        List<Turbine> turbines = Arrays.asList(new Turbine(), new Turbine());
        // PageImpl로 Page 객체 생성
        Page<Turbine> turbinePage = new PageImpl<>(turbines);

        // 페이지 요청 시 Turbine 페이지를 반환
        given(turbineRepository.findAll(Mockito.any(PageRequest.class))).willReturn(turbinePage);

        // when
        Page<Turbine> result = turbineService.findTurbines(0, 10);

        // then
        // 결과 검증
        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        // Mock 메서드 호출 검증
        verify(turbineRepository).findAll(Mockito.any(PageRequest.class));
    }

    @DisplayName("findTurbine 실패 테스트_터빈 없음")
    @Test
    void findTurbineTest2() {
        // given
        long turbineId = 1L;

        // 터빈 아이디가 존재하지 않을 때 예외 날리기 위해 엠티 반환
        given(turbineRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        // when & then
        // 예외 발생
        assertThrows(BusinessLogicException.class, () -> turbineService.findTurbine(turbineId));

        // 실제 호출 됐는지 재확인
        verify(turbineRepository).findById(turbineId);
    }
}

