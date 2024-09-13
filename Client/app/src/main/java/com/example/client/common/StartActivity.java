package com.example.client.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.client.R;
import com.example.client.api.LocalDateTimeDeserializer;
import com.example.client.api.MappingClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.threeten.bp.LocalDateTime;

import java.lang.reflect.Type;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    // NOTE : 뒤로가기 두 번 클릭시 앱 종료  ============================================================
    private long backKeyPressedTime = 0;  // NOTE : 초 저장

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

        // Intent로부터 JSON 문자열을 가져옵니다
        Intent intent = getIntent();
        String jsonBusinessList = intent.getStringExtra("businessListJson");

        // Gson 객체 생성 및 역직렬화 설정
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();

        // JSON 문자열을 BusinessResponse 객체 리스트로 변환
        Type businessListType = new TypeToken<List<MappingClass.BusinessResponse>>() {}.getType();
        List<MappingClass.BusinessResponse> businessList = gson.fromJson(jsonBusinessList, businessListType);

        // 필요한 데이터만 추출
        for (MappingClass.BusinessResponse business : businessList) {
            long businessId = business.getBusinessId();
            String businessTitle = business.getBusinessTitle();
            LocalDateTime createdAt = LocalDateTime.parse(business.getCreatedAt());

            // 필요한 데이터 로그로 확인
            Log.d("BusinessResponse", "Business ID: " + businessId);
            Log.d("BusinessResponse", "Business Title: " + businessTitle);
            Log.d("BusinessResponse", "Created At: " + createdAt);
        }

        start();
    }

    // 시작 버튼.
    private void start() {
        Button btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(v -> {
            startActivity(new Intent(StartActivity.this, BusinessSelectActivity.class));
        });
    }
}