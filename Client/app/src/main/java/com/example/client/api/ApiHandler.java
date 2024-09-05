package com.example.client.api;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiHandler {
    private ApiService apiService;

    public ApiHandler(){
        apiService = RestClient.getClient().create(ApiService.class);
    }

    public void createBusiness(MappingClass.BusinessRequest request, final ApiCallback<MappingClass.BusinessResponse> callback){
        // import 주의! retrofit2.Call 스튜디오 Call 아님
        Call<MappingClass.BusinessResponse> call = apiService.createBusiness(request);
        call.enqueue(new Callback<MappingClass.BusinessResponse>() {
            @Override
            public void onResponse(Call<MappingClass.BusinessResponse> call, Response<MappingClass.BusinessResponse> response) {
                // 2xx 상태 코드로 응답한다면
                if(response.isSuccessful()){
                    callback.onSuccess(response.body());
                }else {
                    try{
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
                callback.onError("Failure : " + t.getMessage() );
            }
        });
    }

}
