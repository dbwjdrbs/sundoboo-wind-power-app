package com.example.client.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.client.R;
import com.example.client.api.ApiCallback;
import com.example.client.api.ApiHandler;
import com.example.client.api.ApiService;
import com.example.client.api.LocalDateTimeDeserializer;
import com.example.client.api.MappingClass;
import com.example.client.api.RestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.threeten.bp.LocalDateTime;

import java.lang.reflect.Type;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    // NOTE : 뒤로가기 두 번 클릭시 앱 종료  ============================================================
    private long backKeyPressedTime = 0;  // NOTE : 초 저장
    private String jsonBusinessList = "";

    @Override
    public void onBackPressed() {
        // NOTE : 2000은 밀리초이다.
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // NOTE :  2초 이내에 뒤로가기 버튼을 한번 더 클릭시 finish()(앱 종료)
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        start();
    }

    // 시작 버튼.
    private void start() {
        Button btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(v -> {

            ApiService apiService = RestClient.getClient().create(ApiService.class);
            ApiHandler apiHandler = new ApiHandler(apiService, this);

            apiHandler.getBusinesses(1, 10, "PAGE_CREATED_AT_DESC", new ApiCallback<List<MappingClass.BusinessResponse>>() {

                @Override
                public void onSuccess(List<MappingClass.BusinessResponse> response) {
                    // Log the response
                    for (MappingClass.BusinessResponse business : response) {
                        Log.d("BusinessResponse", "Business ID: " + business.getBusinessId());
                        Log.d("BusinessResponse", "Business Title: " + business.getBusinessTitle());
                        Log.d("BusinessResponse", "Created At: " + business.getCreatedAt());
                        Log.d("BusinessResponse", "Deleted At: " + business.getDeletedAt());
                    }

                    Gson gson = new Gson();
                    String jsonBusinessList = gson.toJson(response);

                    Intent intent = new Intent(StartActivity.this, BusinessSelectActivity.class);
                    intent.putExtra("businessListJson", jsonBusinessList);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(StartActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}