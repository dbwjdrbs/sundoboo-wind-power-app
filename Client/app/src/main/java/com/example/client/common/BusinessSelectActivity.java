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

import com.example.client.Interface.BusinessSelectItemClickListener;
import com.example.client.R;
import com.example.client.adapter.BusinessSelectAdapter;
import com.example.client.api.ApiCallback;
import com.example.client.api.ApiHandler;
import com.example.client.api.ApiService;
import com.example.client.api.MappingClass;
import com.example.client.api.RestClient;
import com.example.client.data.BusinessData;
import com.example.client.util.MessageDialog;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class BusinessSelectActivity extends AppCompatActivity implements View.OnClickListener, BusinessSelectItemClickListener {
    private ArrayList<BusinessData> list = new ArrayList<>();
    private MessageDialog messageDialog = new MessageDialog();
    private BusinessData businessData;
    private boolean isChecked = false;
    private String isCurrentViewHolder;

    // NOTE : 리사이클러뷰 어뎁터 정의
    BusinessSelectAdapter adapter = new BusinessSelectAdapter(list, this);

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

        list.add(data1);
        list.add(data2);

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
    public void onClick(View v) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_business, null);

        if (v.getId() == R.id.btn_businessAdd) {
            new AlertDialog.Builder(this)
                    .setTitle("사업 추가")
                    .setView(dialogView)
                    .setNegativeButton("취소", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("확인", (dialog, which) -> {
                        EditText businessTitleEt = dialogView.findViewById(R.id.et_businessTitle);
                        String businessTitle = businessTitleEt.getText().toString();
                        if (businessTitle.isEmpty()) {
                            messageDialog.simpleErrorDialog("사업명을 입력해주세요.", this);
                        } else {
                            MappingClass.BusinessRequest request = new MappingClass.BusinessRequest();
                            request.setBusinessTitle(businessTitle);

                            ApiService apiService = RestClient.getClient().create(ApiService.class);
                            ApiHandler apiHandler = new ApiHandler(apiService, this);

                            apiHandler.createBusiness(request, new ApiCallback<Void>() {
                                @Override
                                public void onSuccess(Void response) {
                                    // 요청 성공 처리
                                    long now = System.currentTimeMillis();
                                    Date date = new Date(now);
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 a HH시mm분");
                                    adapter.addItem(new BusinessData(businessTitle, format.format(date)));
                                    adapter.notifyDataSetChanged();
                                    messageDialog.simpleCompleteDialog("사업 등록이 완료되었습니다.", BusinessSelectActivity.this);
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    // 요청 실패 처리
                                    messageDialog.simpleErrorDialog(errorMessage, BusinessSelectActivity.this);
                                }
                            });
                        }
                    })
                    .show();
        }

        if (v.getId() == R.id.btn_businessDelete) {
            if (isChecked) {

//                TODO : 사업 삭제 로직 deleteBusiness(businessId) 매개 변수로 받는 사업Id를 삭제
                MappingClass.DeleteBusiness request = new MappingClass.DeleteBusiness();
                request.setBusinessId(1);

                ApiService apiService = RestClient.getClient().create(ApiService.class);
                ApiHandler apiHandler = new ApiHandler(apiService, this);
                apiHandler.deleteBusiness(request.getBusinessId());

                adapter.removeItem(Integer.parseInt(isCurrentViewHolder));
                messageDialog.simpleCompleteDialog("사업 삭제가 완료되었습니다.", this);
                adapter.notifyDataSetChanged();
            } else {
                messageDialog.simpleErrorDialog("사업이 선택 되지 않았습니다.", this);
            }
        }
    }


    // INFO : 체크박스 이벤트
    @Override
    public void onBusinessItemClick(BusinessData businessData, int pos) {
        if (businessData != null) {
            isChecked = true;
            isCurrentViewHolder = String.valueOf(pos);
            this.businessData = new BusinessData(businessData.getTitle(), businessData.getCreatedAt());
        } else {
            isChecked = false;
        }
    }
}