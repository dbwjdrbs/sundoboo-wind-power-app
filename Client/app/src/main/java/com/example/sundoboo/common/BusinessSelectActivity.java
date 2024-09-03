package com.example.sundoboo.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.client.R;
import com.example.sundoboo.adapter.BusinessSelectAdapter;
import com.example.sundoboo.data.BusinessData;

import java.util.ArrayList;

public class BusinessSelectActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<BusinessData> list;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(BusinessSelectActivity.this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_select);

        BusinessData data1 = new BusinessData("가산 해상풍력단지", "2024년 4월 24일 오전 10시 33분");
        BusinessData data2 = new BusinessData("보령 해상풍력단지", "2024년 4월 24일 오전 10시 33분");
        BusinessData data3 = new BusinessData("울산 부유식 해상풍력단지", "2024년 4월 24일 오전 10시 32분");

        list = new ArrayList<>();
        list.add(data1);
        list.add(data2);
        list.add(data3);

        BusinessSelectAdapter adapter = new BusinessSelectAdapter(list);
        RecyclerView recyclerView = findViewById(R.id.rv_businessSelect);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btn_businessAdd).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final EditText input = new EditText(this);
        input.setHint("사업명을 입력하세요"); // 힌트 설정

        // EditText 크기 조정
        input.setLayoutParams(new ViewGroup.LayoutParams(100, 100));

        if (v.getId() == R.id.btn_businessAdd) {
            new AlertDialog.Builder(this)
                    .setTitle("사업 추가")
                    .setView(R.layout.addboxdialog)
                    .setNegativeButton("취소", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("확인", (dialog, which) -> {
                        String name = input.getText().toString();
                        if (!name.isEmpty()) {
                            Toast.makeText(BusinessSelectActivity.this, "등록된 사업명: " + name, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BusinessSelectActivity.this, "사업명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
    }

    // NOTE : OnClickEvent를 Switch 문으로 일괄 처리

}