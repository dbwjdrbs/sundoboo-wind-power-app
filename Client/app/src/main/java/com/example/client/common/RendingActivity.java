package com.example.client.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.animation.core.StartOffset;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.example.client.R;
import com.example.client.api.ApiCallback;
import com.example.client.api.ApiHandler;
import com.example.client.api.ApiService;
import com.example.client.api.MappingClass;
import com.example.client.api.RestClient;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class RendingActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSIONS = 100;  // NOTE : 카메라 펄미션 요청 코드
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
        setContentView(R.layout.activity_rending);
        setPermission(); // NOTE : 권한 요청 메서드
    }

    // NOTE : 권한 요청 메서드
    private void setPermission() {
        // NOTE : 카메라와 위치 권한 체크
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // NOTE : 권한이 없을 경우 권한 요청하는 매서드
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    REQUEST_CODE_PERMISSIONS);
        } else {
            // NOTE : 권한이 이미 있는 경우, 다음 작업을 진행 하도록 하는 매서드
            performActionWithPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // NOTE : 권한이 허용된 경우, 다음 작업을 진행하도록 하는 매서드
                performActionWithPermissions();
            } else {
                // NOTE : 권한이 거부된 경우, 앱 종료
                Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void performActionWithPermissions() {
        startActivity(new Intent(RendingActivity.this, StartActivity.class));
    }
}