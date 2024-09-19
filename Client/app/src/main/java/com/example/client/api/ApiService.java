package com.example.client.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

// 실제 HTTP 요청을 생성하는 부분
// 엔드포인트에 대한 요청을 메서드로 정의
// Retrofit이 APiService 인터페이스를 구현해서 HTTP 요청을 처리 하고 응답을 해당 데이터 타입 으로 변환 해줌
public interface ApiService {
    @POST("/businesses/registration")
    Call<MappingClass.BusinessResponse2> createBusiness(@Body MappingClass.BusinessRequest request);

    @GET("/businesses/{business-id}")
    Call<MappingClass.BusinessResponse> getBusiness(@Path("business-id") long businessId);

    @DELETE("/businesses/{business-id}")
    Call<Void> deleteBusiness(@Path("business-id") long businessId);

    @GET("/businesses")
    Call<BusinessResponseWrapper> getBusinesses(
            @Query("page") int page,
            @Query("size") int size,
            @Query("direction") String direction
    );

    @GET("/businesses")
    Call<BusinessResponseWrapper> getBusinesses(
            @Query("page") int page,
            @Query("size") int size,
            @Query("direction") String direction,
            @Query("keyword") String keyword
    );

    @GET("/turbines/{turbine-id}")
    Call<MappingClass.TurbineResponse> getTurbine(@Path("turbine-id") long turbineId);

    @GET("/turbines")
    Call<List<MappingClass.TurbineResponse>> getTurbines();

    @POST("/scores/registration")
    Call<MappingClass.BusinessScoreResponse> createBusinessScore(@Body MappingClass.BusinessScorePost request);

    @GET("/scores/search/{business-id}")
    Call<MappingClass.BusinessScoreResponse> getBusinessScore(@Path("business-id") long businessScoreId);
    @POST("/locations")
    Call<Void> createLocation(@Body MappingClass.LocationPostRequest request);

    @DELETE("/locations/business/{business-id}")
    Call<Void> deleteLocationsByBusinessId(@Path("business-id") long businessId);

    @GET("/locations/search/dd")
    Call<MappingClass.DdResponse> getDD(@Query("latitude") String latitude, @Query("longitude") String longitude);

    @PATCH("/locations")
    Call<Void> patchLocation(@Body MappingClass.LocationPatchRequest request);
}