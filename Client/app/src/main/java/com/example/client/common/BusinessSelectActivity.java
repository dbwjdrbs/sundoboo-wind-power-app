package com.example.client.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.client.Interface.BusinessSelectItemClickListener;
import com.example.client.R;
import com.example.client.adapter.BusinessSelectAdapter;
import com.example.client.api.ApiCallback;
import com.example.client.api.ApiHandler;
import com.example.client.api.ApiService;
import com.example.client.api.LocalDateTimeDeserializer;
import com.example.client.api.MappingClass;
import com.example.client.api.RestClient;
import com.example.client.data.BusinessData;
import com.example.client.util.MessageDialog;
import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

        // Intent로부터 JSON 문자열을 가져옵니다
        Intent intent = getIntent();
        String jsonBusinessList = intent.getStringExtra("businessListJson");

        // Gson 객체 생성 및 역직렬화 설정
        Gson gson = new GsonBuilder()
                .create();

        // JSON 문자열을 BusinessResponse 객체 리스트로 변환
        Type businessListType = new TypeToken<List<MappingClass.BusinessResponse>>() {
        }.getType();
        List<MappingClass.BusinessResponse> businessList = gson.fromJson(jsonBusinessList, businessListType);

        // 필요한 데이터만 추출
        for (MappingClass.BusinessResponse business : businessList) {
            long businessId = business.getBusinessId();
            String businessTitle = business.getBusinessTitle();
            String createdAt = business.getCreatedAt();

            list.add(new BusinessData(businessId, businessTitle, createdAt));
            // 필요한 데이터 로그로 확인
            Log.d("BusinessResponse", "Business ID: " + businessId);
            Log.d("BusinessResponse", "Business Title: " + businessTitle);
            Log.d("BusinessResponse", "Created At: " + business.getCreatedAt());
        }

        RecyclerView recyclerView = findViewById(R.id.rv_businessSelect);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        EditText et_search = findViewById(R.id.et_business_search);

        Button btn_searchRefresh = findViewById(R.id.btn_refreshSearch);
        btn_searchRefresh.setOnClickListener(view -> {
            adapter.searchMode(list);
            adapter.notifyDataSetChanged();
            et_search.setText(null);
        });
        // NOTE : 엔터키 쳐서 검색처리하기
        et_search.setOnEditorActionListener((v, keyCode, keyEvent) -> {
            String keyword = et_search.getText().toString();
            if (keyCode == EditorInfo.IME_ACTION_DONE) {
                if (keyword.equals("") || keyword == null) {
                    messageDialog.simpleErrorDialog("검색어를 입력해주세요.", this);
                } else {
                    ApiService apiService = RestClient.getClient().create(ApiService.class);
                    ApiHandler apiHandler = new ApiHandler(apiService, this);
                    apiHandler.getBusinesses(1, 10, "PAGE_CREATED_AT_DESC", keyword, new ApiCallback<List<MappingClass.BusinessResponse>>() {
                        @Override
                        public void onSuccess(List<MappingClass.BusinessResponse> response) {
                            ArrayList<BusinessData> list = new ArrayList<>();
                            for (MappingClass.BusinessResponse businessResponse : response) {
                                list.add(new BusinessData(businessResponse.getBusinessId(), businessResponse.getBusinessTitle(), businessResponse.getCreatedAt()));
                            }
                            adapter.searchMode(list);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });
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

                            // BUG : 리스폰스가 안가져와짐
                            apiHandler.createBusiness(request, new ApiCallback<MappingClass.BusinessResponse2>() {
                                @Override
                                public void onSuccess(MappingClass.BusinessResponse2 response) {
                                    // 요청 성공 처리
                                    long businessId = response.getData().getBusinessId();
                                    String title = response.getData().getBusinessTitle();
                                    String createdAt = response.getData().getCreatedAt();

                                    adapter.addItem(new BusinessData(businessId, title, createdAt));
                                    adapter.notifyDataSetChanged();
                                    Log.d("완료", "사업생성완료");
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
                MappingClass.DeleteBusiness request = new MappingClass.DeleteBusiness();
                request.setBusinessId(businessData.getBusinessId());

                ApiService apiService = RestClient.getClient().create(ApiService.class);
                ApiHandler apiHandler = new ApiHandler(apiService, this);
                apiHandler.deleteBusiness(request.getBusinessId());

                adapter.removeItem(Integer.parseInt(isCurrentViewHolder));
                isChecked = false;
                CheckBox checkBox = findViewById(R.id.checkBox);
                checkBox.setSelected(false);
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
            this.businessData = businessData; // 수정 필요
        } else {
            isChecked = false;
        }
    }
}