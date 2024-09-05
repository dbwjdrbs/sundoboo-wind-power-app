package com.example.client.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

// 실제 HTTP 요청을 생성하는 부분
// 엔드포인트에 대한 요청을 메서드로 정의
// Retrofit이 APiService 인터페이스를 구현해서 HTTP 요청을 처리 하고 응답을 해당 데이터 타입 으로 변환 해줌
public interface ApiService {
    @POST("/business")
    Call<MappingClass.BusinessResponse> createBusiness(@Body MappingClass.BusinessRequest request);

    @GET("/businesses/{business-id}")
    Call<MappingClass.BusinessResponse> getBusiness(@Path("business-id") long businessId);

    @DELETE("/businesses/{business-id}")
    Call<Void> deleteBusiness(@Path("business-id") long businessId);

    @GET("/businesses")
    Call<List<MappingClass.BusinessResponse>> getBusinesses();

    @GET("/turbines/{turbine-id}")
    Call<MappingClass.TurbineResponse> getTurbine(@Path("turbine-id") long turbineId);

    @GET("/turbines")
    Call<List<MappingClass.TurbineResponse>> getTurbine();

    @POST("/businessScore")
    Call<MappingClass.BusinessScoreResponse> createBusinessScore(@Body MappingClass.BusinessScoreRequest request);

    @GET("/businessScore/{business-score-id}")
    Call<MappingClass.BusinessScoreResponse> getBusinessScore(@Path("business-score-id") long businessScoreId);

    @GET("/businessScore")
    Call<List<MappingClass.BusinessScoreResponse>> getBusinessScores();

    @GET("/businesses")
    Call<MappingClass.BusinessResponse> getHelloWorld();
}

