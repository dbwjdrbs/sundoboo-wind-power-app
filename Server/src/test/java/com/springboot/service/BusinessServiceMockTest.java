package com.springboot.service;


import com.springboot.business.entity.Business;
import com.springboot.business.repository.BusinessRepository;
import com.springboot.business.service.BusinessService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

//** 참고 사항 junit 테스트 사용하기 위해 build.gradle 환경 설정하기

@Transactional
// spring 사용 안하고 junit 모키토 기능을 사용하기 위해 추가하는 에너테이션
@ExtendWith(MockitoExtension.class)
public class BusinessServiceMockTest {

    @Mock
    private BusinessRepository businessRepository;

    @InjectMocks
    private BusinessService businessService;

    @DisplayName("createdBusiness 성공테스트")
    @Test
    public void createdBusiness(){

        //given -> 테스트를 위한 준비 단계
        Business business = new Business();
        String bTitle = "룰루상사";
        business.setBusinessTitle(bTitle);


        //when --> 실제 메서드 호출하는 단계
        // 비지니스 타이틀이 존재하는지 중복을 확인하는 메서드를 통해 엠티를 반환함으로 예외 상황이 통과된걸 명시한다
        given(businessRepository.findByBusinessTitle(Mockito.anyString())).willReturn(Optional.empty());
        // 예외 통과 후 레파지에 비지니스 클래스타입이면 저장하고 내가 설정한 객체인 비지니스를 반환하게함
        given(businessRepository.save(Mockito.any(Business.class))).willReturn(business);

        //then ---> 결과를 검증하는 단계
        assertThat(businessService.createBusiness(business), is(business));
        // 해당 코드와 같은 역할(비교)
        // assertEquals(expected, actual);

    }

    // deleted에 대한 성공 테스트를 하기 어렵기 때문에 예외가 잘 발생하는지 확인하는 테스트짬
    @DisplayName("deletedBusiness 실패테스트")
    @Test
    public void deletedBusiness(){

        //given
        // 테스트 하기 위해 비지니스 객체 생성
        Business business = new Business();
        Long businessId = 1L;
        business.setBusinessId(businessId);

        //when
        // 실제 테스트할 메서드를 호출함 -> 실패 테스트이므로 어떤 예외상황을 만났을 때 통과하는지 확인한거
        // 즉, 어떤걸 반환하면 안됨 반환하면 예외를 던지는거와 같음 그래서 아무것도 반환안하는 엠티를 반환하게끔 설정
        given(businessRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        //then
        // 위에서 아무것도 반환안했으니 삭제할게 없어서 예외 던져지는게 성공해서 실패한 상황을 통과한 실패 테스트임
        assertThrows(BusinessLogicException.class, () -> businessService.deleteBusiness(businessId));

    }

    @DisplayName("getBusinesses 성공테스트")
    @Test
    public void getBusinesses(){

        // given
        // 성공 테스트를 위한 더미데이터 생성 후 리스트로 설정
        Business business1 = new Business();
        Business business2 = new Business();

        Page<Business> page = new PageImpl<>(Arrays.asList(business1, business2));

        // when
        // 비지니스레포에 삭제된 상태인 비지니스를 제외하고 페이지로 리턴함
        given(businessRepository.findByDeletedAtIsNull(
                Mockito.any(PageRequest.class))).willReturn(page);

        //then
        // 비지니스 서비스에 getBusinesses 메서드를 통해 내가 만든 페이지 객체와 비교함
        assertThat(businessService.getBusinesses(2, 10, "PAGE_CREATED_AT_ASC"), is(page));

    }


}
