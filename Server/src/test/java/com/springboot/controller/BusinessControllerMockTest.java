package com.springboot.controller;

import com.google.gson.Gson;
import com.springboot.business.dto.BusinessDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.startsWith;
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
                // createBusinessTest의 경우 서버가 응답으로 Location 헤더를 "/businesses/1"로 설정
//              // HTTP header에 추가된 Location의 문자열 값이 “/businesses/1”로 시작하는지 검증
                .andExpect(header().string("Location", startsWith("/businesses/1")));
    }

    @Test
    void deleteBusinessTest() throws Exception {

        // =================================== (1) createBusiness()를 이용한 테스트 데이터 생성 시작
        //given
        // 디티오 객체 생성하고 타이틀 설정
        BusinessDto.Post post = new BusinessDto.Post();
        post.setBusinessTitle("Test Business");


        // 사용하려면 build.gradle implementation 추가해야함
        // 데이터를 데려옴 (createBusiness()의 response에 전달되는 Location header 값을 가져오는 로직 )
        String content = gson.toJson(post);

        //when
        ResultActions createdActions =
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
        // =================================== (1) createBusiness()를 이용한 테스트 데이터 생성 끝

        // 데이터를 데려옴 ( postMember()의 response에 전달되는 Location header 값을 가져오는 로직 )
        String location = createdActions.andReturn().getResponse().getHeader("Location");

        mockMvc.perform(
                        delete(location) // Location에서 BusinessId를 추출해 삭제 요청
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )// then
                .andExpect(status().isNoContent());  // HTTP 204 No Content 응답 확인

        // (3) 추가 검증: 해당 ID의 멤버가 실제로 삭제되었는지 확인
        mockMvc.perform(
                        get(location) // 삭제된 멤버에 대해 다시 조회 요청
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound()); //멤버가 삭제되었으므로 HTTP 404 Not Found 응답 확인
    }

    @Test
    void getBusinessTest() throws Exception {
        // =================================== (1) createBusiness()를 이용한 테스트 데이터 생성 시작
        //given
        // 디티오 객체 생성하고 타이틀 설정
        BusinessDto.Post post = new BusinessDto.Post();
        post.setBusinessTitle("Test Business");


        // 사용하려면 build.gradle implementation 추가해야함
        // 데이터를 데려옴 (createBusiness()의 response에 전달되는 Location header 값을 가져오는 로직 )
        String content = gson.toJson(post);

        //when
        ResultActions createdActions =
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
        // =================================== (1) createBusiness()를 이용한 테스트 데이터 생성 끝
        String location = createdActions.andReturn().getResponse().getHeader("Location");

        //when /then
        mockMvc.perform(
                        //얻은 location 값을 get(locaion)으로 전달 -> get(...)의 URI로 전달
                        get(location)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk()) //  기대하는 HTTP status가 200 OK인지를 검증
                // getMember() 핸들러 메서드에서 리턴하는 response body(JSON 형식)의 각 프로퍼티(email, name, phone)의 값을 검증하는 기능
                // jsonPath() 메서드를 통해 response body(JSON 형식)의 각 프로퍼티 중에서 응답으로 전달받는 email 값이 request body로 전송한 email과 일치하는지 검증
                .andExpect(jsonPath("$.data.businessTitle").value(post.getBusinessTitle()));
    }

    @Test
    void getBusinessesTest() throws Exception {
        // =================================== (1) createBusiness()를 이용한 테스트 데이터 생성 시작
        //given
        // 디티오 객체 생성하고 타이틀 설정
        BusinessDto.Post post = new BusinessDto.Post();
        post.setBusinessTitle("Test Business");

        BusinessDto.Post post1 = new BusinessDto.Post();
        post1.setBusinessTitle("Test Business1");


        // 사용하려면 build.gradle implementation 추가해야함
        // 데이터를 데려옴 (createBusiness()의 response에 전달되는 Location header 값을 가져오는 로직 )
        String content = gson.toJson(post);
        String content1 = gson.toJson(post1);


        //when
        ResultActions createdActions =
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
        mockMvc.perform(
                // HTTP request에 대한 정보들
                // post() 메서드를 통해 HTTP POST METHOD와 request URL을 설정
                post("/businesses/registration")
                        // accept() 메서드를 통해 클라이언트 쪽에서 리턴 받을 응답 데이터 타입으로 JSON 타입을 설정
                        .accept(MediaType.APPLICATION_JSON)
                        // contentType() 메서드를 통해 서버 쪽에서 처리 가능한 Content Type으로 JSON 타입을 설정
                        .contentType(MediaType.APPLICATION_JSON)
                        // content() 메서드를 통해 request body 데이터를 설정
                        .content(content1)
        );
        // =================================== (1) createBusiness()를 이용한 테스트 데이터 생성 끝
        // getMembers 메서드는 여러 멤버를 반환하는 로직이기 때문에, Location 헤더를 사용할 필요가 없는거 참고

        //when
        // 비지니스 목록을 가져오는 겟요청
        ResultActions actions = mockMvc.perform(
                get("/businesses")// 기본 url에 대한 겟요청
                        .param("page", "1") // 요청 파라미터로 페이지 번호 설정
                        .param("size", "2") // 요청 파라미터로 페이지 사이즈 설정
                        .param("direction", "PAGE_CREATED_AT_ASC") // 방향 파라미터 추가
                        .accept(MediaType.APPLICATION_JSON) // 응답으로 Json 요청
        );
        //then
        // 응답 상태 코드와 응답 데이터를 검증
        actions
                .andExpect(status().isOk())// 응답 200ok 확인
                .andExpect(jsonPath("$.data[0].businessTitle").value(post.getBusinessTitle()))
                .andExpect(jsonPath("$.data[1].businessTitle").value(post1.getBusinessTitle()));
    }
}