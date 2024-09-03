package com.example.sundoboo.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.client.R;

public class MainActivity extends AppCompatActivity {
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
    // =============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, BusinessSelectActivity.class);
            startActivity(intent);
            finish();
        });
    }

}