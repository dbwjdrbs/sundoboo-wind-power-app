package com.example.client.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.example.client.Interface.ItemClickListener;
import com.example.client.R;
import com.example.client.adapter.BusinessSelectAdapter;
import com.example.client.data.BusinessData;
import com.example.client.util.MessageDialog;

import java.util.ArrayList;

public class BusinessSelectActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener {
    private ArrayList<BusinessData> list;
    private MessageDialog messageDialog = new MessageDialog();
    private BusinessData businessData;
    private boolean isChecked = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(BusinessSelectActivity.this, StartActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_select);

        // NOTE : 사업 리스트 더미 데이터
        BusinessData data1 = new BusinessData("가산 해상풍력단지", "2024년 4월 24일 오전 10시 33분");
        BusinessData data2 = new BusinessData("보령 해상풍력단지", "2024년 4월 24일 오전 10시 33분");
        BusinessData data3 = new BusinessData("울산 부유식 해상풍력단지", "2024년 4월 24일 오전 10시 32분");

        list = new ArrayList<>();
        list.add(data1);
        list.add(data2);
        list.add(data3);

        // NOTE : 리사이클러뷰 어뎁터 정의
        BusinessSelectAdapter adapter = new BusinessSelectAdapter(list, this);
        RecyclerView recyclerView = findViewById(R.id.rv_businessSelect);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        EditText et_search = findViewById(R.id.et_business_search);
        String etContent = et_search.getText().toString();

        // NOTE : 엔터키 쳐서 검색처리하기
        et_search.setOnEditorActionListener((v, keyCode, keyEvent) -> {
            if (keyCode == EditorInfo.IME_ACTION_DONE) {
                if (etContent.equals("") || etContent == null) {
                    messageDialog.simpleErrorDialog("검색어를 입력해주세요.", this);
                } else {
                    // TODO : 비즈니스 로직 생성
                }
            }
            return false;
        });

        findViewById(R.id.btn_businessAdd).setOnClickListener(this);
        findViewById(R.id.btn_businessDelete).setOnClickListener(this);
    }

    // INFO : 온 클릭 이벤트 처리
    @Override
    public void onClick(View v) {
        // BUG : 입력한 사업명을 추출하기 위해 View를 따로 선언했다.
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_business, null);

        if (v.getId() == R.id.btn_businessAdd) {
            new AlertDialog.Builder(this)
                    .setTitle("사업 추가")
                    // BUG : dialogView 추가
                    .setView(dialogView)
                    .setNegativeButton("취소", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("확인", (dialog, which) -> {
                        // BUG : EditText 추출 및 String 값 추출
                        EditText businessTitleEt = dialogView.findViewById(R.id.et_businessTitle);
                        String businessTitle = businessTitleEt.getText().toString();
                        if (businessTitle.isEmpty()) {
                            messageDialog.simpleErrorDialog("사업명을 입력해주세요.", this);
                        } else {
                            // TODO : 비즈니스 로직 생성
                            messageDialog.simpleCompleteDialog("사업 등록이 완료되었습니다.", this);
                        }
                    })
                    .show();
        }

        if (v.getId() == R.id.btn_businessDelete) {
            if (isChecked) {
                messageDialog.simpleCompleteDialog("사업 삭제가 완료되었습니다.", this);
                // TODO : 비즈니스 로직 생성
            } else {
                messageDialog.simpleErrorDialog("사업이 선택 되지 않았습니다.", this);
            }
        }
    }

    // INFO : 체크박스 이벤트
    @Override
    public void onBusinessItemClick(BusinessData businessData) {
        if (businessData != null) {
            isChecked = true;
            this.businessData = new BusinessData(businessData.getTitle(), businessData.getCreatedAt());
            Toast.makeText(this, "Title: " + businessData.getTitle(), Toast.LENGTH_SHORT).show();
        } else {
            isChecked = false;
        }
    }
}