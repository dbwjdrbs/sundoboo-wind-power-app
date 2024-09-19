// package com.springboot.service;


// import com.springboot.business.entity.Business;
// import com.springboot.business.service.BusinessService;
// import com.springboot.businessscore.dto.BusinessScoreDto;
// import com.springboot.businessscore.entity.BusinessScore;
// import com.springboot.businessscore.repository.BusinessScoreRepository;
// import com.springboot.businessscore.service.BusinessScoreService;
// import com.springboot.exception.BusinessLogicException;
// import com.springboot.exception.ExceptionCode;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;

// import javax.transaction.Transactional;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

// import static org.hamcrest.Matchers.is;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.BDDMockito.given;
// import static org.mockito.Mockito.*;

// @Transactional
// @ExtendWith(MockitoExtension.class)
// public class BusinessScoreServiceMockTest {
//     @Mock
//     private BusinessScoreRepository businessScoreRepository;

//     @Mock
//     private BusinessService businessService;

//     @InjectMocks
//     private BusinessScoreService businessScoreService;


//     @DisplayName("post 성공 테스트")
//     @Test
//     public void postBusinessScoreTest1() {
//         // given
//         // 비지니스 스코어에서 dto를 받아 수정하므로 각 디티오 요소를 지정
//         BusinessScoreDto.Post requestBody = new BusinessScoreDto.Post();
//         requestBody.setBusinessId(1L);
//         requestBody.setBusinessScoreTitle("Excellent");
//         requestBody.setScoreList1(10);
//         requestBody.setScoreList2(20);
//         requestBody.setScoreList3(30);
//         requestBody.setScoreList4(40);
//         requestBody.setObserverName("John Doe");

//         // create 할 때 예외 통과하기 위해 비지니스 아이디 설정
//         Business business = new Business();
//         business.setBusinessId(1L);

//         // Mock 설정
//         given(businessService.verifyExistBusiness(Mockito.anyLong())).willReturn(business);
//         given(businessScoreRepository.findByBusinessScoreTitle(requestBody.getBusinessScoreTitle())).willReturn(Optional.empty());

//         // 새로운 BusinessScore 객체 생성
//         // 테스트 코드에서 BusinessScore 객체와 requestBody DTO 객체를 따로 생성한 이유는
//         // 실제 서비스 로직이 DTO 받아서 엔티티로 변환하는 과정을 테스트에 반영한거 참고
//         BusinessScore savedBusinessScore = new BusinessScore();
//         savedBusinessScore.setBusiness(business);
//         savedBusinessScore.setBusinessScoreTitle(requestBody.getBusinessScoreTitle());
//         savedBusinessScore.setScoreList1(requestBody.getScoreList1());
//         savedBusinessScore.setScoreList2(requestBody.getScoreList2());
//         savedBusinessScore.setScoreList3(requestBody.getScoreList3());
//         savedBusinessScore.setScoreList4(requestBody.getScoreList4());
//         savedBusinessScore.setObserverName(requestBody.getObserverName());

//         // 엔티티 값을 레파지에 저장
//         given(businessScoreRepository.save(Mockito.any(BusinessScore.class))).willReturn(savedBusinessScore);


//         // when
//         // 실제 서비스 메서드를 호출하고 그 결과를 받아옴
//         BusinessScore result = businessScoreService.createBusinessScore(requestBody);

//         // then
//         // 검증 진행 널 아님 확인
//         assertNotNull(result);
//         // 내가 설정한 값 맞는지 비교
//         assertEquals("Excellent", result.getBusinessScoreTitle());
//         assertEquals(10, result.getScoreList1());
//         assertEquals(20, result.getScoreList2());
//         assertEquals(30, result.getScoreList3());
//         assertEquals(40, result.getScoreList4());
//         assertEquals("John Doe", result.getObserverName());
//         // 재확인 -> 실제로 해당 메서드가 사용 되었는지
//         verify(businessService).verifyExistBusiness(requestBody.getBusinessId());
//         verify(businessScoreRepository).findByBusinessScoreTitle(requestBody.getBusinessScoreTitle());
//         verify(businessScoreRepository).save(Mockito.any(BusinessScore.class));
//     }

//     @DisplayName("post 실패 테스트_타이틀 중복")
//     @Test
//     public void postBusinessScoreTest2() {
//         // given
//         // 비즈니스 스코어 DTO 설정
//         BusinessScoreDto.Post requestBody = new BusinessScoreDto.Post();
//         requestBody.setBusinessId(1L);
//         requestBody.setBusinessScoreTitle("Duplicate Title"); // 중복된 타이틀
//         requestBody.setScoreList1(10);
//         requestBody.setScoreList2(20);
//         requestBody.setScoreList3(30);
//         requestBody.setScoreList4(40);
//         requestBody.setObserverName("John Doe");

//         // 비즈니스 객체 생성
//         Business business = new Business();
//         business.setBusinessId(1L);

//         // Mock 설정
//         // 비지니스는 존재
//         given(businessService.verifyExistBusiness(Mockito.anyLong())).willReturn(business);
//         // 이미 존재하는 타이틀 예외 걸리게끔 설정
//         given(businessScoreRepository.findByBusinessScoreTitle(requestBody.getBusinessScoreTitle()))
//                 .willReturn(Optional.of(new BusinessScore()));

//         // when & then
//         // 예외가 던져지는지 확인
//         assertThrows(BusinessLogicException.class, () -> businessScoreService.createBusinessScore(requestBody));


//         // 추가 검증 (메서드 실제 호출되다가 저장하는 부분에서 호출안됐는지 확인)
//         verify(businessService).verifyExistBusiness(requestBody.getBusinessId());
//         verify(businessScoreRepository).findByBusinessScoreTitle(requestBody.getBusinessScoreTitle());
//         verify(businessScoreRepository, never()).save(Mockito.any(BusinessScore.class)); // save 호출 안됨
//     }

//     @DisplayName("post 실패 테스트_비즈니스 없음")
//     @Test
//     public void postBusinessScoreTest3() {
//         // given
//         // 비즈니스 스코어 DTO 설정
//         BusinessScoreDto.Post requestBody = new BusinessScoreDto.Post();
//         requestBody.setBusinessId(1L);
//         requestBody.setBusinessScoreTitle("Valid Title");
//         requestBody.setScoreList1(10);
//         requestBody.setScoreList2(20);
//         requestBody.setScoreList3(30);
//         requestBody.setScoreList4(40);
//         requestBody.setObserverName("John Doe");

//         // Mock 설정
//         // 본래 강제로 예외를 던지기 보다는 예외가 잘 발생하는지 롹인해야하는데 서비스 코드를 willThrow 밖에 못쓰도록 짜서 여기서는 쩔수 없이 강제로 예외 발생시키고
//         // 이후 비지니스가 없을 때 서비스 로직을 검증함
//         given(businessService.verifyExistBusiness(Mockito.anyLong())).willThrow(new BusinessLogicException(ExceptionCode.BUSINESS_NOT_FOUND));

//         // when & then
//         // 예외가 던져지는지 확인
//         assertThrows(BusinessLogicException.class, () -> businessScoreService.createBusinessScore(requestBody));


//         // 추가 검증 (메서드 호출 확인)
//         verify(businessService).verifyExistBusiness(requestBody.getBusinessId());
//         verify(businessScoreRepository, never()).findByBusinessScoreTitle(Mockito.anyString()); // 이 메서드는 호출되지 않아야 함
//         verify(businessScoreRepository, never()).save(Mockito.any(BusinessScore.class)); // save 호출 안됨
//     }
//     // 없는 값 들어오면 예외 던지게끔 코드 보완해야함
//     @DisplayName("findScore 성공 테스트")
//     @Test
//     public void findScoreSuccessTest() {
//         // given
//         // 비지니스 객체 생성하고 아이디 할당
//         long businessId = 1L;
//         Business business = new Business();
//         business.setBusinessId(businessId);
//         List<BusinessScore> scores = Arrays.asList(new BusinessScore(), new BusinessScore());
//         Page<BusinessScore> scorePage = new PageImpl<>(scores, PageRequest.of(0, 10), scores.size());

//         // Mock 설정
//         given(businessScoreRepository.findByBusiness_BusinessId(Mockito.eq(businessId), Mockito.any(PageRequest.class))).willReturn(scorePage);

//         // when
//         Page<BusinessScore> result = businessScoreService.findScore(businessId, 0, 10);

//         // then
//         assertNotNull(result);
//         assertEquals(2, result.getContent().size()); // 반환된 점수의 개수 확인
//         verify(businessScoreRepository).findByBusiness_BusinessId(Mockito.eq(businessId), Mockito.any(PageRequest.class));
//     }
// }

