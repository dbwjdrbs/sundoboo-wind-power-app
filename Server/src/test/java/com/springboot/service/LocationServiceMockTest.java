package com.springboot.service;

import com.springboot.business.entity.Business;
import com.springboot.business.service.BusinessService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.location.entity.Location;
import com.springboot.location.repository.LocationRepository;
import com.springboot.location.service.LocationService;
import com.springboot.turbine.entitiy.Turbine;
import com.springboot.turbine.repository.TurbineRepository;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@Transactional
@ExtendWith(MockitoExtension.class)
public class LocationServiceMockTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private TurbineRepository turbineRepository;

    @Mock
    private BusinessService businessService;

    @InjectMocks
    private LocationService locationService;

    @DisplayName("post 성공 테스트")
    @Test
    public void postLocationSuccessTest(){

        //given
        // 비지니스 아이디와 터빈을 갖고 있는 로케이션 객체 생성
        Location location = new Location();
        Business business  = new Business();
        Long businessId = 1L;
        business.setBusinessId(businessId);
        location.setBusiness(business);

        Long turbineId = 1L;
        Turbine turbine = new Turbine();
        turbine.setTurbineId(turbineId);
        location.setTurbine(turbine);


        //when
        // 처음에 비지니스와 터빈이 널인 예외를 통과해야함
        // 처음안 사실 목 테스트는 테스트 범위를 넘으면 테스트를 통과할 수 없다 그래서 불필요한 목은 사용불가!
        // 비지니스가 널인 예외를 통과하는건 필요한게 맞지만 비지니스 서비스 코드를 사용하고 있으로 불필요
//        given(businessRepository.findById(Mockito.anyLong())).willReturn(Optional.of(business));
        given(turbineRepository.findById(Mockito.anyLong())).willReturn(Optional.of(turbine));

        // 서비스 로직에서 찾은 비지니스를 할당하는걸 통과해야함
        given(businessService.verifyExistBusiness(Mockito.anyLong())).willReturn(business);
        given(locationRepository.save(Mockito.any(Location.class))).willReturn(location);

        //then
        assertThat(locationService.postLocation(location), is(location));
    }


    @DisplayName("post 실패 테스트 터빈.ver")
    @Test
    public void locationPostFailedTest(){
        //given
        // 비지니스 아이디와 터빈을 갖고 있는 로케이션 객체 생성
        // 로케이션에 터빈 아이디와 비지니스 아이디를 갖고 있어야
        // 서비스 메서드의 null인지 체크하고 예외 던지는 로직을 통과하고 그 다음으로 넘어갈 수 있음
        Location location = new Location();

        Business business  = new Business();
        Long businessId = 1L;
        business.setBusinessId(businessId);
        location.setBusiness(business);

        Long turbineId = 1L;
        Turbine turbine = new Turbine();
        turbine.setTurbineId(turbineId);
        location.setTurbine(turbine);

        //when
        // 로케이션 포스트의 터빈 검증이 우선적으로 이루어지므로 터빈만 검증
        // 터빈 레파지에서 터빈 아이디가 없다면 아무것도 반환하지 않으므로 예외가 던져질것
        given(turbineRepository.findById(turbineId)).willReturn(Optional.empty());

        // 위 검증코드에서 걸리므로 예외 코드 던져질 것
        assertThrows(BusinessLogicException.class, () -> locationService.postLocation(location));
    }

    @DisplayName("patch 성공 테스트")
    @Test
    public void locationPatchSuccessTest(){

        /**
         * 생성 이후 업데이트를 검증 해야 함
         * 하나의 비즈니스를 만들고 일단 비즈니스를 등록 함 -> 완료
         * 이후에 등록한 비즈니스를 수정하고, 수정 요청(테스트 대상인 로케이션 패치를 통해) 보낸ㅁ
         * 이후 수정되었는지를 검증하는게 이 테스트의 목적
         */
        //given
        // 비지니스 아이디와 터빈을 갖고 있는 로케이션 객체 생성
        // 로케이션에 터빈 아이디와 비지니스 아이디를 갖고 있어야 수정 가능
        Location location = new Location();
        // 수정하고 싶은 예시 데이터 설정
        location.setLatitude("37.5665");
        location.setLongitude("126.978");
        location.setCity("Seoul");
        location.setIsland("Jeju");

        Business business  = new Business();
        Long businessId = 1L;
        business.setBusinessId(businessId);
        location.setBusiness(business);

        Long turbineId = 1L;
        Turbine turbine = new Turbine();
        turbine.setTurbineId(turbineId);
        location.setTurbine(turbine);


        //when
        given(locationRepository.findById(Mockito.anyLong())).willReturn(Optional.of(location));
        given(businessService.verifyExistBusiness(Mockito.anyLong())).willReturn(business);
        given(turbineRepository.findById(Mockito.anyLong())).willReturn(Optional.of(turbine));
        given(locationRepository.save(Mockito.any(Location.class))).willReturn(location);

        //then
        // 내가 위에서 설정한 코드가 잘들어간지 비교하고 확인한 후 레파지토리에 저장하고 비교함
        // 내가 위에서 설정한 로케이션과 벨류를 비교함
        assertThat(locationService.patchLocation(location).getLatitude(), is("37.5665"));
        assertThat(locationService.patchLocation(location).getLongitude(), is("126.978"));
        assertThat(locationService.patchLocation(location).getIsland(), is("Jeju"));
        assertThat(locationService.patchLocation(location).getCity(), is("Seoul"));

        // 해당코드는 왜 있어도 되고 없어도 되는가? 결정적인 로직이 아니라그런가? 그거를 모키토 자체 내에서 거르는건가?
        // 저장하는 로직은 업데이트하는 메서드에서 중요 로직이 아니기 때문에 필요없다고한다 (우선)
        assertThat(locationRepository.save(location), is(location));

    }

    @DisplayName("patch 실패 테스트 1")
    @Test
    public void locationPatchFailedTest1(){

        //given
        // 비지니스 아이디와 터빈을 갖고 있는 로케이션 객체 생성
        // 로케이션에 터빈 아이디와 비지니스 아이디를 갖고 있어야 수정 가능
        Location location = new Location();
        // 수정하고 싶은 예시 데이터 설정
        location.setLatitude("37.5665");
        location.setLongitude("126.978");
        location.setCity("Seoul");
        location.setIsland("Jeju");

        Business business  = new Business();
        Long businessId = 1L;
        business.setBusinessId(businessId);
        location.setBusiness(business);

        Long turbineId = 1L;
        Turbine turbine = new Turbine();
        turbine.setTurbineId(turbineId);
        location.setTurbine(turbine);

        //when
        given(locationRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        //then
        // 위 검증코드에서 걸리므로 예외 코드 던져질 것
        assertThrows(BusinessLogicException.class, () -> locationService.patchLocation(location));
    }
    @DisplayName("patch 실패 테스트 2")
    @Test
    public void locationPatchFailedTest2(){

        //given
        // 비지니스 아이디와 터빈을 갖고 있는 로케이션 객체 생성
        // 로케이션에 터빈 아이디와 비지니스 아이디를 갖고 있어야 수정 가능
        Location location = new Location();

        Business business  = new Business();
        Long businessId = 1L;
        business.setBusinessId(businessId);
        location.setBusiness(business);

        // 비지니스와 터빈을 갖지 않은 테스트 객체 설정
        Location tempLocation = new Location();
        tempLocation.setLocationId(1L);

        //when
        // 터빈 존재여부를 검증하는 코드에 도달하기까지 통과해야함
        given(locationRepository.findById(Mockito.anyLong())).willReturn(Optional.of(tempLocation));
        given(businessService.verifyExistBusiness(Mockito.anyLong())).willReturn(new Business());


        //then
        // 위의 테스트를 통과하면 로케이션의 터빈을 검증하는 코드에 걸리는지 확인 -> 터질 것을 예상
        assertThrows(BusinessLogicException.class, () -> locationService.patchLocation(location));
    }

    @DisplayName("findLocations 성공 테스트")
    @Test
    void findLocationsSuccessTest() {
        // given
        // 리스트안에 임의로 로케이션 생성
        List<Location> locations = Arrays.asList(new Location(), new Location());
        Page<Location> locationPage = new PageImpl<>(locations);
        // 로케이션 레파지에서 사용되는 메서드 통해 로케이션 페이지 반환
        given(locationRepository.findAll(Mockito.any(PageRequest.class))).willReturn(locationPage);

        // when
        // 서비스 findLocations 메서드 통해 result 페이지 생성
        Page<Location> result = locationService.findLocations(0, 10);

        // then
        // 해당 코드가 result가 null이 아닌 경우에만 추가적인 검증을 진행
        assertNotNull(result);
        //  result 객체의 getContent() 메서드를 호출하여 반환된 리스트의 크기가 2인지 확인하는 검증 코드
        assertEquals(2, result.getContent().size());
        // verify 메서드는 Mockito 기능 중 하나로, 특정 메서드가 실제로 호출되었는지, 얼마나 호출되었는지 등을 확인
        // 실제로 호출 됐는지 재검증
        verify(locationRepository).findAll(Mockito.any(PageRequest.class));
    }

    @DisplayName("findLocations 실패 테스트 - 빈 페이지")
    @Test
    void findLocationsFailedTest() {
        // given
        // 빈페이지 생성
        Page<Location> emptyPage = new PageImpl<>(List.of());
        // 페이지 타입 받으면 빈페이지로 무조건 반환
        given(locationRepository.findAll(Mockito.any(PageRequest.class))).willReturn(emptyPage);

        // when
        // 서비스 findLocations 메서드 통해 result 페이지 생성
        Page<Location> result = locationService.findLocations(0, 10);
        // then
        // 해당 코드가 result가 null이 아닌 경우에만 추가적인 검증을 진행
        assertNotNull(result);
        // result 빈페이지인지 검증 빈페이지 (빈페이지가 널값은 아니다! 빈페이지는 다른 예외)
        assertTrue(result.isEmpty());
        // locationRepository는 테스트에서 모의(mock)된 객체, findAll 메서드는 PageRequest를 인자로 받음
        // Mockito.any(PageRequest.class)는 PageRequest 타입의 어떤 값이든 상관없이 검증하겠다는 의미.
        // 즉, 메서드가 호출될 때 전달된 PageRequest 인자에 대해 구체적인 값을 검사하지 않고, PageRequest 타입의 인자가 사용되었는지를 확인!
        verify(locationRepository).findAll(Mockito.any(PageRequest.class));
    }

    @DisplayName("findLocation 성공 테스트")
    @Test
    void findLocationSuccessTest() {
        // given
        // 비지니스 객체 생성하고 아이디 할당
        long businessId = 1L;
        Business business = new Business();
        business.setBusinessId(businessId);
        // 리스트 생성
        List<Location> locations = Arrays.asList(new Location(), new Location());
        Page<Location> locationPage = new PageImpl<>(locations);

        // businessService verifyExistBusiness 검증을 먼저 통과해야함
        given(businessService.verifyExistBusiness(businessId)).willReturn(business);
        // 호출 시 첫 번째 인자가 business와 정확히 일치해야 한다는 것을 지정하는거
        // 즉, locationRepository.findByBusiness 메서드가 호출될 때, 첫 번째 인자가 business 변수와 같아야 한다는 조건을 설정
        given(locationRepository.findByBusiness(Mockito.eq(business), Mockito.any(PageRequest.class))).willReturn(locationPage);

        // when
        Page<Location> result = locationService.findLocation(businessId, 0, 10);

        // then
        // 널 검증,  result 객체의 getContent() 메서드를 호출하여 반환된 리스트의 크기가 2인지 확인하는 검증 코드
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        // verifyExistBusiness, findByBusiness 각각 실제로 쓰였는지 재확인하고
        // findByBusiness 메서드가 호출될 때, 첫 번째 인자가 business 변수와 같은지 확인
        verify(businessService).verifyExistBusiness(businessId);
        verify(locationRepository).findByBusiness(Mockito.eq(business), Mockito.any(PageRequest.class));
    }

    @DisplayName("findLocation 실패 테스트 - 비즈니스 없음")
    @Test
    void findLocationBusinessFailedTest() {
        // given --> 모키토 설정
        // 예외 검증 위해 businessId 생성
        long businessId = 1L;
        // 본래 강제로 예외를 던지기 보다는 예외가 잘 발생하는지 롹인해야하는데 서비스 코드를 willThrow 밖에 못쓰도록 짜서 여기서는 쩔수 없이 강제로 예외 발생시키고
        // 이후 비지니스가 없을 때 서비스 로직을 검증함
        given(businessService.verifyExistBusiness(businessId)).willThrow(new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND));

        // when & then --> 테스트 실행
        // 실제로 locationService.findLocation 메서드를 호출할 때 BusinessLogicException 발생하는지 확인
        assertThrows(BusinessLogicException.class, () -> locationService.findLocation(businessId, 0, 10));
        // 실제로 해당 메서드 사용됐는지 재검증
        verify(businessService).verifyExistBusiness(businessId);
        // 위에서 비지니스 예외 던져저서 서비스 코드에서는 findByBusiness 사용안됨, 그거를 실제 사용 안됐는지 확인하는 메서드
        // ->never()는 Mockito 메서드 호출 검증 기능 중 하나로, 특정 메서드가 호출되지 않았음을 검증할 때 사용
        verify(locationRepository, never()).findByBusiness(Mockito.any(Business.class), Mockito.any(PageRequest.class));

        // 모키토 설정과 테스트 실행 차이점
        // 어떤 메서드를 사용했을 때 원하는 아웃풋이 나오게끔 목 환경 설정을함
        // 이후 위 설정에 따라 해당 서비스 메서드를 실행했을 때 실제로 원하는 아웃풋이 나오는지 확인하는거가 테스트 실행이다
    }


    @DisplayName("deleteLocation 실패 테스트 - 로케이션 없음")
    @Test
    void deleteLocationNotFailedTest() {
        // given
        long locationId = 1L;
        given(locationRepository.findById(locationId)).willReturn(Optional.empty());

        // when & then
        assertThrows(BusinessLogicException.class, () -> locationService.deleteLocation(locationId));
        // 재검증
        verify(locationRepository).findById(locationId);
        //  never() 메서드로 delete 실제로 실행 안됐는지 확인
        verify(locationRepository, never()).delete(Mockito.any(Location.class));
    }


}
