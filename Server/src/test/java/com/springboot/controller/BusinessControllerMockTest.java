package com.springboot.controller;

import com.google.gson.Gson;
import com.springboot.business.dto.BusinessDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.NestedTestConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
// SpringBootTest 사용시 테스트 실행 때 필요한 모든 빈이 포함된 Application Context 생성.
// 이를 통해 실제 애플리케이션 환경에서의 통합 테스트를 수행가능
@SpringBootTest
// MockMvc 같은 기능을 사용하기 위해서 @AutoConfigureMockMvc 애너테이션을 반드시 추가
//  테스트에 필요한 MockMvc 인스턴스를 자동으로 설정해서
//  이를 통해 HTTP 요청을 시뮬레이션하고, 컨트롤러의 동작을 검증할 수 있음
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BusinessControllerMockTest {


    // DI로 주입받은 MockMvc는 Tomcat 같은 서버를 실행하지 않고 Spring 기반 애플리케이션의
    // Controller를 테스트할 수 있는 완벽한 환경을 지원해 주는 일종의 Spring MVC 테스트 프레임워크
    @Autowired
    private MockMvc mockMvc;

    // Gson은 Java 객체를 JSON 형식으로 변환하는 라이브러리, 테스트 코드에서는 MemberDto.Post 객체를 JSON 문자열로 변환하여 HTTP 요청의 본문에 포함시킴
    //Postman에서는 사용자가 직접 JSON 형식을 작성하여 요청 본문에 넣지만, 테스트 코드에서는 이 과정을 코드로 자동화함
    @Autowired
    private Gson gson;

    @Test
    @Order(1)
    void createBusinessTest() throws Exception {

        //given
        // 디티오 객체 생성하고 타이틀 설정
        BusinessDto.Post post = new BusinessDto.Post();
        post.setBusinessTitle("Test Business");


        // 사용하려면 build.gradle implementation 추가해야함
        // 데이터를 데려옴 (createBusiness()의 response에 전달되는 Location header 값을 가져오는 로직 )
        String content = gson.toJson(post);

        //when
        ResultActions actions =
                // MockMvc로 테스트 대상 Controller의 핸들러 메서드에 요청을 전송하기 위해서는
                // 기본적으로 perform() 메서드를 호출해야 하며, perform() 메서드 내부에 Controller 호출을 위한 세부적인 정보들이 포함
                mockMvc.perform(
                        // HTTP request에 대한 정보들
                        // post() 메서드를 통해 HTTP POST METHOD와 request URL을 설정
                        post("/businesses/registration")
                                // accept() 메서드를 통해 클라이언트 쪽에서 리턴 받을 응답 데이터 타입으로 JSON 타입을 설정
                                .accept(MediaType.APPLICATION_JSON)
                                // contentType() 메서드를 통해 서버 쪽에서 처리 가능한 Content Type으로 JSON 타입을 설정
                                .contentType(MediaType.APPLICATION_JSON)
                                // content() 메서드를 통해 request body 데이터를 설정
                                .content(content)
                );
        // MockMvc의 perform() 메서드는 ResultActions 타입의 객체를 리턴하는데,
        // 이 ResultActions 객체를 이용해서 우리가 전송한 request 대한 검증을 수행하는게 -> then
        //then
        actions
                // andExpect() 메서드를 통해 파라미터로 입력한 매처(Matcher)로 예상되는 기대 결과를 검증함
                // status().isCreated()를 통해 response status가 201(Created)인지 매치시키고 있다 즉, 백엔드 측에 리소스인 회원 정보가 잘 생성(저장)되었는지를 검증
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.businessTitle").value("Test Business")); // 응답 데이터 검증 추가
    }

    @Test
    @Order(2)
    void deleteBusinessTest() throws Exception {

        // Given: 비즈니스를 생성하기 위한 DTO 객체를 준비
        BusinessDto.Post post = new BusinessDto.Post();
        post.setBusinessTitle("Test Business"); // 비즈니스 제목 설정

        // DTO를 JSON으로 변환
        String content = gson.toJson(post);

        // When: 비즈니스를 생성하기 위한 POST 요청을 MockMvc를 통해 전송
        ResultActions createdActions = mockMvc.perform(
                post("/businesses/registration") // 비즈니스 등록 URL
                        .accept(MediaType.APPLICATION_JSON) // JSON 형식으로 응답 수신
                        .contentType(MediaType.APPLICATION_JSON) // 요청 본문 타입을 JSON으로 설정
                        .content(content) // 요청 본문에 JSON 데이터 추가
        );

        // Then: 비즈니스 생성 요청의 응답 상태 코드 확인
        createdActions.andExpect(status().isCreated()); // 상태 코드 201 Created 확인

        // Location 헤더가 없을 경우를 대비해 비즈니스 ID를 직접 생성
        long businessId = 2L; // 테스트 데이터에 맞는 ID를 설정 (필요에 따라 조정 가능)

        // 비즈니스 삭제 요청
        mockMvc.perform(
                        delete("/businesses/{business-id}", businessId) // 비즈니스 ID를 포함한 DELETE 요청
                                .accept(MediaType.APPLICATION_JSON) // JSON 형식으로 응답 수신
                                .contentType(MediaType.APPLICATION_JSON) // 요청 본문 타입을 JSON으로 설정
                )
                .andExpect(status().isNoContent()); // HTTP 204 No Content 응답 확인

        // 삭제된 비즈니스에 대한 조회 요청
        mockMvc.perform(
                        get("/businesses/{business-id}", businessId) // 삭제된 비즈니스를 조회하기 위한 GET 요청
                                .accept(MediaType.APPLICATION_JSON) // JSON 형식으로 응답 수신
                                .contentType(MediaType.APPLICATION_JSON) // 요청 본문 타입을 JSON으로 설정
                )
                .andExpect(status().isNotFound()); // 비즈니스가 삭제되었으므로 HTTP 404 Not Found 응답 확인
    }

    @Test
    @Order(3)
    void getBusinessTest() throws Exception {
        // Given: 비즈니스를 생성하기 위한 DTO 객체를 준비
        BusinessDto.Post post = new BusinessDto.Post();
        post.setBusinessTitle("Test Business"); // 비즈니스 제목 설정

        // DTO를 JSON으로 변환
        String content = gson.toJson(post);

        // When: 비즈니스를 생성하기 위한 POST 요청을 MockMvc를 통해 전송
        ResultActions createdActions = mockMvc.perform(
                post("/businesses/registration") // 비즈니스 등록 URL
                        .accept(MediaType.APPLICATION_JSON) // JSON 형식으로 응답 수신
                        .contentType(MediaType.APPLICATION_JSON) // 요청 본문 타입을 JSON으로 설정
                        .content(content) // 요청 본문에 JSON 데이터 추가
        );

        // Then: 비즈니스 생성 요청의 응답 상태 코드 확인
        createdActions.andExpect(status().isCreated()); // 상태 코드 201 Created 확인

        // 비즈니스 ID를 하드코딩으로 설정 (테스트 환경에 맞게 조정)
        long businessId = 3L; // 적절한 비즈니스 ID를 설정 (등록된 비즈니스 ID에 맞게 조정)

        // 비즈니스 조회 요청
        mockMvc.perform(
                        get("/businesses/{business-id}", businessId) // 비즈니스 ID를 포함한 GET 요청
                                .accept(MediaType.APPLICATION_JSON) // JSON 형식으로 응답 수신
                )
                .andExpect(status().isOk()) // 기대하는 HTTP 상태가 200 OK인지 검증
                .andExpect(jsonPath("$.data.businessTitle").value(post.getBusinessTitle())); // 응답 데이터 검증
    }

    @Test
    @Order(4)
    void getBusinessesTest() throws Exception {
        // =================================== (1) createBusiness()를 이용한 테스트 데이터 생성 시작
        // Given: 비즈니스를 생성하기 위한 DTO 객체를 준비
        BusinessDto.Post post = new BusinessDto.Post();
        post.setBusinessTitle("Test Business"); // 첫 번째 비즈니스 제목 설정

        BusinessDto.Post post1 = new BusinessDto.Post();
        post1.setBusinessTitle("Test Business1"); // 두 번째 비즈니스 제목 설정

        // DTO를 JSON으로 변환
        String content = gson.toJson(post);
        String content1 = gson.toJson(post1);

        // When: 비즈니스 등록 요청을 MockMvc를 통해 전송
        // 첫 번째 비즈니스 등록
        mockMvc.perform(
                post("/businesses/registration") // 비즈니스 등록 URL
                        .accept(MediaType.APPLICATION_JSON) // JSON 형식으로 응답 수신
                        .contentType(MediaType.APPLICATION_JSON) // 요청 본문 타입을 JSON으로 설정
                        .content(content) // 요청 본문에 JSON 데이터 추가
        );

        // 두 번째 비즈니스 등록
        mockMvc.perform(
                post("/businesses/registration") // 비즈니스 등록 URL
                        .accept(MediaType.APPLICATION_JSON) // JSON 형식으로 응답 수신
                        .contentType(MediaType.APPLICATION_JSON) // 요청 본문 타입을 JSON으로 설정
                        .content(content1) // 요청 본문에 JSON 데이터 추가
        );
        // =================================== (1) createBusiness()를 이용한 테스트 데이터 생성 끝

        // When: 비즈니스 목록을 가져오는 GET 요청
        ResultActions actions = mockMvc.perform(
                get("/businesses") // 비즈니스 목록 요청 URL
                        .param("page", "1") // 요청 파라미터로 페이지 번호 설정
                        .param("size", "2") // 요청 파라미터로 페이지 사이즈 설정
                        .param("direction", "PAGE_CREATED_AT_ASC") // 방향 파라미터 추가
                        .param("keyword", "") // 검색 키워드 추가 (빈 문자열로 설정)
                        .accept(MediaType.APPLICATION_JSON) // 응답으로 JSON 요청
        );

        // Then: 응답 상태 코드와 응답 데이터를 검증
        actions
                .andExpect(status().isOk()) // 응답 상태가 200 OK인지 확인
                .andExpect(jsonPath("$.data[0].businessTitle").value(post.getBusinessTitle())) // 첫 번째 비즈니스 제목 검증
                .andExpect(jsonPath("$.data[1].businessTitle").value(post1.getBusinessTitle())); // 두 번째 비즈니스 제목 검증
    }
}