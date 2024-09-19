package com.example.client.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.client.R;
import com.example.client.api.ApiCallback;
import com.example.client.api.ApiHandler;
import com.example.client.api.ApiService;
import com.example.client.api.MappingClass;
import com.example.client.api.RestClient;
import com.google.gson.Gson;

public class ApiActivity extends AppCompatActivity {

    private long locationId;
    private long businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        // Intent에서 locationId와 businessId 받기
//            -1이 들어온다면 UnityPlayerActivity에서 가져온 id 값이 잘못된 걸 확인할 수 있다.
        Intent intent = getIntent();
        locationId = intent.getLongExtra("locationId", -1); // 기본값 -1
        businessId = intent.getLongExtra("businessId", -1); // 기본값 -1

        if (locationId == -1 || businessId == -1) {
            Log.e("ApiActivity", "Invalid locationId or businessId");
            // 에러 처리 필요
            return;
        }

        ApiService apiService = RestClient.getClient().create(ApiService.class);
        ApiHandler apiHandler = new ApiHandler(apiService, this);

        // 패치 요청을 보낼 리퀘스트 바디 생성
        MappingClass.LocationPatchRequest request = new MappingClass.LocationPatchRequest();
        request.setLocationId(locationId);
        request.setBusinessId(businessId);
        request.setTurbineId(1); // 터빈 아이디 설정

        apiHandler.patchLocation(request, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void response) {
                // 성공적으로 업데이트된 경우 처리
                Log.d("ApiHandler", "Location updated successfully");
            }

            @Override
            public void onError(String errorMessage) {
                // 오류 처리
                Log.e("ApiHandler", "Error: " + errorMessage);
            }
        });
    }
}