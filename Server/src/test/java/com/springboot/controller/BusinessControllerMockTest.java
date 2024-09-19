package com.springboot.controller;

import com.springboot.business.controller.BusinessController;
import com.springboot.business.dto.BusinessDto;
import com.springboot.business.entity.Business;
import com.springboot.business.mapper.BusinessMapper;
import com.springboot.business.repository.BusinessRepository;
import com.springboot.business.service.BusinessService;
import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.exception.BusinessLogicException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BusinessControllerMockTest {

    @Mock
    private BusinessService businessService;

    @Mock
    private BusinessMapper businessMapper;

    @Mock
    private BusinessRepository businessRepository;

    @InjectMocks
    private BusinessController businessController;

    @DisplayName("createBusiness 성공 테스트")
    @Test
    public void createBusinessTest1() {
        // given
        BusinessDto.Post requestBody = new BusinessDto.Post();
        requestBody.setBusinessTitle("Test Business");

        // 비지니스 객체 생성 후 이름 설정
        Business business = new Business();
        business.setBusinessId(1L);

        // Mock 설정
        // 매퍼로 변환하고 서비스 통해 생성하는 것까지함
        given(businessMapper.businessPostDtoToBusiness(requestBody)).willReturn(business);
        given(businessService.createBusiness(business)).willReturn(business);

        // when
        // 컨트롤러의 createBusiness 메서드를 호출하여 비즈니스 생성 로직을 실행
        ResponseEntity<?> responseEntity = businessController.createBusiness(requestBody);

        // then
        // 생성된 비즈니스 객체의 상태 코드가 201 CREATED인지 확인
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        // 응답 헤더에 Location 설정되어 있는지 확인 (비즈니스 객체의 URI)
        assertNotNull(responseEntity.getHeaders().getLocation());

        // verify -> 해당 메서드가 역할을 제대로 수행했는지를 확인하는 것
        //  비즈니스 DTO가 매퍼를 통해 Business 객체로 변환되었는지 확인
        verify(businessMapper).businessPostDtoToBusiness(requestBody);
        // 비즈니스 객체가 서비스에 의해 실제로 생성되었는지 확인
        verify(businessService).createBusiness(business);
    }
    @DisplayName("createBusiness 실패 테스트_비즈니스 생성 실패")
    @Test
    public void createBusinessTest2() {
        // ㄹㅇ 실패함 어케 짤지 다시 생각좀 헤보자

        // given
        BusinessDto.Post requestBody = new BusinessDto.Post();
        requestBody.setBusinessTitle("Test Business");

        Business business = new Business();
        business.setBusinessId(1L);

        // Mock 설정
        // dto를 따로 받는 모키토는 없어서 아무거나 받아도 비지니스를 반환하도록 설정
        given(businessMapper.businessPostDtoToBusiness(Mockito.any())).willReturn(business);
        // 비즈니스 생성 시 예외 발생하도록 설정
        given(businessRepository.findByBusinessTitle(business.getBusinessTitle())).willReturn(Optional.of(business));

        // when & then
        // 예외가 발생하는지 확인
        assertThrows(BusinessLogicException.class, () -> businessController.createBusiness(requestBody));

        // 매퍼와 서비스 메서드가 실제로 호출되었는지 확인
        verify(businessMapper).businessPostDtoToBusiness(requestBody);
        verify(businessService).createBusiness(business);
    }
    @DisplayName("getBusiness 성공 테스트")
    @Test
    public void getBusinessTest() {
        // given
        // 비지니스 아이디 생성
        long businessId = 1L;

        // Mock 비즈니스 객체 생성
        Business business = new Business();
        business.setBusinessId(businessId);
        business.setBusinessTitle("Test Business");
        // 디티오 객체 생성
        BusinessDto.Response responseDto = new BusinessDto.Response();
        responseDto.setBusinessTitle("Test Business");

        // Mock 설정
        // 서비스 예외 통과하도록 설정
        given(businessService.verifyExistBusiness(Mockito.anyLong())).willReturn(business);
        given(businessMapper.businessToBusinessResponseDto(business)).willReturn(responseDto);

        // when
        // 컨트롤러의 createBusiness 메서드를 호출하여 비즈니스 생성 로직을 실행
        ResponseEntity<?> responseEntity = businessController.getBusiness(businessId);

        // then
        //  생성된 비즈니스 객체의 상태 코드가 200 OK 확인
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        // 아래꺼 안쓰니 통과함 왜지 ? 이유 알고 적기
        // responseEntity.getBody()가 SingleResponseDto의 인스턴스인지 확인
        // 만약 getBusiness 메서드가 반환하는 응답 본문이 실제로 SingleResponseDto가 아니면, 이 검증에서 실패해서임
        assertTrue(responseEntity.getBody() instanceof SingleResponseDto);
//
//        주석 처리된 코드가 포함되면 응답의 타입과 내용이 정확한지 검증하기 때문에 통과하지 못하는 것이고,
//                이를 제외하면 검증이 없어서 통과하는 것이라 검증 실패한거나 다름없기 때문에
//                디티오 객체 생성해서 다시 테스트함 --> 테스트 통과!
        SingleResponseDto<?> body = (SingleResponseDto<?>) responseEntity.getBody();
        assertEquals("Test Business", ((BusinessDto.Response) body.getData()).getBusinessTitle());

        // verify
        // 실제로 사용되서 제 역할 했는지 확인
        verify(businessService).verifyExistBusiness(businessId);
        verify(businessMapper).businessToBusinessResponseDto(business);
    }

    @DisplayName("getBusinesses 성공 테스트")
    @Test
    public void getBusinessesTest() {
        // 왜 계속 미스매치 되는지 확인하고 코드 수정하기
        // given
        String direction = "asc";

        // Mock 비즈니스 객체 생성
        Business business1 = new Business();
        business1.setBusinessId(1L);
        business1.setBusinessTitle("Business 1");

        Business business2 = new Business();
        business2.setBusinessId(2L);
        business2.setBusinessTitle("Business 2");

        List<Business> businessList = Arrays.asList(business1, business2);
        Page<Business> businessPage = new PageImpl<>(businessList);

        // BusinessDto.Response 객체 생성
        BusinessDto.Response responseDto1 = new BusinessDto.Response();
        responseDto1.setBusinessId(business1.getBusinessId());
        responseDto1.setBusinessTitle(business1.getBusinessTitle());

        BusinessDto.Response responseDto2 = new BusinessDto.Response();
        responseDto2.setBusinessId(business2.getBusinessId());
        responseDto2.setBusinessTitle(business2.getBusinessTitle());

        // Mock 설정
        given(businessService.getBusinesses(- 1, 10, direction)).willReturn(businessPage);
        given(businessMapper.businessToBusinessResponseDtos(businessList)).willReturn(Arrays.asList(responseDto1, responseDto2));

        // when
        ResponseEntity<?> responseEntity = businessController.getBusinesses(-1, 10, direction);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        MultiResponseDto<?> body = (MultiResponseDto<?>) responseEntity.getBody();
        assertEquals(2, body.getData().size());
        assertEquals("Business 1", ((BusinessDto.Response) body.getData().get(0)).getBusinessTitle());
        assertEquals("Business 2", ((BusinessDto.Response) body.getData().get(1)).getBusinessTitle());

        // verify
        verify(businessService).getBusinesses(-1, 10, direction);
        verify(businessMapper).businessToBusinessResponseDtos(businessList);
    }

}
