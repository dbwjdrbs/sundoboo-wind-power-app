package com.example.client.api;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiHandler {
    private ApiService apiService;

    public ApiHandler(ApiService apiService) {
        this.apiService = RestClient.getClient().create(ApiService.class);
    }

    public void getBusinesses() {

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


    // Hello World API 요청
    public void getHelloWorld(final ApiCallback<MappingClass.BusinessResponse> callback) {
        // ApiService의 getHelloWorld 메서드를 호출하여 API 요청을 생성합니다.
        Call<MappingClass.BusinessResponse> call = apiService.getHelloWorld();
        // 비동기 API 요청을 수행합니다.
        call.enqueue(new Callback<MappingClass.BusinessResponse>() {

            @Override
            public void onResponse(Call<MappingClass.BusinessResponse> call,
                                   Response<MappingClass.BusinessResponse> response) {
                if (response.isSuccessful()) {
                    // API 요청이 성공적이라면 콜백의 onSuccess 메서드를 호출하여 응답을 전달합니다.
                    callback.onSuccess(response.body());
                } else {
                    // API 요청이 실패했다면 에러 메시지를 생성하여 콜백의 onError 메서드를 호출합니다.
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        callback.onError("Error: " + errorMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onError("Error: Unable to parse error body");
                    }
                }
            }

            @Override
            public void onFailure(Call<MappingClass.BusinessResponse> call, Throwable t) {
                // API 요청이 실패했다면 에러 메시지를 생성하여 콜백의 onError 메서드를 호출합니다.
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }
}

