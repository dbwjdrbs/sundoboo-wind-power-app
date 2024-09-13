package com.example.client.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiHandler {
    private ApiService apiService;
    private Context context;

    public ApiHandler(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    public void createBusiness(MappingClass.BusinessRequest request, final ApiCallback<MappingClass.BusinessResponse> callback) {
        // import 주의! retrofit2.Call 스튜디오 Call 아님
        Call<MappingClass.BusinessResponse> call = apiService.createBusiness(request);
        call.enqueue(new Callback<MappingClass.BusinessResponse>() {
            @Override
            public void onResponse(Call<MappingClass.BusinessResponse> call, Response<MappingClass.BusinessResponse> response) {
                // 2xx 상태 코드로 응답한다면
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        // 4xx. 5xx 상태 코드일 때 서버에서 전달하는 에러메세지나 데이터에 접근
                        // 이때 errorBody는 ResponseBody객체로 반환 되어 직렬화나 구체적인 내용의 추가 처리가 필요함.
                        callback.onError("Error : " + errorMessage);

                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onError("Error : Unable to parse error body");
                    }
                }
            }

            @Override
            public void onFailure(Call<MappingClass.BusinessResponse> call, Throwable t) {
                callback.onError("Failure : " + t.getMessage());
            }
        });
    }

    public void getBusinesses(int page, int size, String direction, ApiCallback<List<MappingClass.BusinessResponse>> callback) {
        Call<BusinessResponseWrapper> call = apiService.getBusinesses(page, size, direction);
        call.enqueue(new Callback<BusinessResponseWrapper>() {
            @Override
            public void onResponse(Call<BusinessResponseWrapper> call, Response<BusinessResponseWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Log JSON 응답 확인
                    String responseBody = new Gson().toJson(response.body());
                    Log.d("API Response", responseBody);

                    List<MappingClass.BusinessResponse> businessList = response.body().getData();
                    callback.onSuccess(businessList);
                } else {
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BusinessResponseWrapper> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }
}