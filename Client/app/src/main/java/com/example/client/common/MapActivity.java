package com.example.client.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.R;
import com.example.client.adapter.TurbinesSelectAdapter;
import com.example.client.data.TurbinesData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    private Marker mMarker;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // SupportMapFragment를 찾아서 지도가 준비되었을 때 콜백을 받을 수 있도록 설정
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // NOTE : 버튼 온클릭 리스너 선언
        findViewById(R.id.btn_posInput).setOnClickListener(this);
        findViewById(R.id.btn_arView).setOnClickListener(this);
        findViewById(R.id.btn_scoreInput).setOnClickListener(this);
        findViewById(R.id.btn_scoreList).setOnClickListener(this);
    }

    // ================================================================ GoogleMap
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;  // GoogleMap 객체를 초기화

        // 위치 권한이 부여된 경우 현재 위치를 지도에 표시
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);  // 지도에 내 위치를 표시하도록 설정
            getDeviceLocation();  // 현재 위치를 가져와서 지도에 표시
        } else {
            // 위치 권한을 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        // 지도 클릭 리스너 설정
        mMap.setOnMapClickListener(latLng -> {
            // 기존 마커가 있는 경우 삭제
            if (mMarker != null) {
                mMarker.remove();
            }
            // 커스텀 마커 만들기
            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("사용자 위치")
                    .icon(icon)  // 아이콘 설정
                    .alpha(0.8f)); // 마커의 투명도 설정 (0.0f ~ 1.0f)
            // 클릭한 위치의 위도, 경도 정보를 DB에 저장
            saveLocationToDatabase(latLng);
        });
    }

    private void getDeviceLocation() {
        // 위치 권한이 부여된 경우에만 현재 위치를 가져올 수 있음
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        // 현재 위치를 LatLng 객체로 변환
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addCircle(new CircleOptions()
                                .center(currentLatLng)
                                .radius(10)  // 반경을  (미터 단위)
                                .strokeColor(Color.BLUE)  // 원 테두리 색상
                                .fillColor(0x220000FF)    // 원 내부 색상 (반투명 파란색)
                                .strokeWidth(5));  // 테두리 두께
//                        // 카메라를 현재 위치로 이동 및 확대
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                    }
                });
    }

    private void saveLocationToDatabase(LatLng latLng) {
        // 위도
        double latitude = latLng.latitude;
        // 경도
        double longitude = latLng.longitude;

        // 위도 도 분 초 변환
        double[] latDMS = decimalToDMS(latitude);
        // 경도 도 분 초 변환
        double[] lonDMS = decimalToDMS(longitude);

        int latDegrees = (int) latDMS[0];
        int latMinutes = (int) latDMS[1];
        int latSeconds = (int) latDMS[2];

        int lonDegrees = (int) lonDMS[0];
        int lonMinutes = (int) lonDMS[1];
        int lonSeconds = (int) lonDMS[2];

        // 아래에 데이터베이스에 저장하는 로직을 추가
        // 예: SQLiteDatabase.insert(...)
        Toast.makeText(this, "Latitude DMS: " + latDegrees + "° " + latMinutes + "' " + latSeconds + "''", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Longitude DMS: " + lonDegrees + "° " + lonMinutes + "' " + lonSeconds + "''", Toast.LENGTH_SHORT).show();
    }

    private double[] decimalToDMS(double decimal) {
        int degrees = (int) decimal;
        double fractional = Math.abs(decimal - degrees);
        int minutes = (int) (fractional * 60);
        int seconds = (int) ((fractional * 3600) % 60);
        return new double[]{degrees, minutes, seconds};
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 권한 요청 결과를 처리
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되면 현재 위치를 가져옴
                getDeviceLocation();
            }
        }
    }
    // ================================================================================


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_posSelect) {

        }

        if (v.getId() == R.id.btn_posInput) {
            showDialog_posInput();
        }

        if (v.getId() == R.id.btn_arView) {
            showDialog_arView();
        }

        if (v.getId() == R.id.btn_scoreInput) {
            showDialog_scoreInput();
        }

        if (v.getId() == R.id.btn_scoreList) {
            showDialog_scoreInput();
        }

    }

    private void showDialog_posInput() {
        Dialog dialog;
        dialog = new Dialog(MapActivity.this);
        dialog.setContentView(R.layout.dialog_pos_input);

        dialog.show();

        TextView tv_title = dialog.findViewById(R.id.di_pos_title);
        TextView tv_content = dialog.findViewById(R.id.di_pos_content);
        Button btn_close = dialog.findViewById(R.id.di_pos_closeButton);
        Button btn_DD = dialog.findViewById(R.id.di_pos_firstButton);
        Button btn_DMS = dialog.findViewById(R.id.di_pos_secondButton);

        tv_title.setText("좌표 입력 방식");
        tv_content.setText("좌표 입력 방식을 선택해주세요. 소수점 형식(DD) 또는 도분초 형식(DMS)을 사용할 수 있습니다.");

        btn_DD.setText("DD (위도, 경도)");
        btn_DMS.setText("DMS (도분초)");

        btn_DD.setTextColor(Color.parseColor("#478A81"));
        btn_DMS.setTextColor(Color.parseColor("#A87DB0"));

//        btn_DD.setVisibility(View.GONE);

        btn_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btn_DD.setOnClickListener(v -> {
            dialog.dismiss();
            showBottomDialog_DD();
        });

        btn_DMS.setOnClickListener(v -> {
            dialog.dismiss();
            showBottomDialog_DMS();
        });
    }
    private ArrayList<TurbinesData> tb_list;

    private void showDialog_arView() {
        Dialog dialog;
        dialog = new Dialog(MapActivity.this);
        dialog.setContentView(R.layout.dialog_arview);

        // NOTE : 풍력 발전기 모델 더미 데이터
        TurbinesData data1 = new TurbinesData("두산중공업 풍력 발전기", "Doosan Wind Power Generator");
        TurbinesData data2 = new TurbinesData("유니슨 풍력 발전기", "Unison Wind Power Generator");

        tb_list = new ArrayList<>();
        tb_list.add(data1);
        tb_list.add(data2);

        // NOTE : 리사이클러뷰 어뎁터 정의
        TurbinesSelectAdapter adapter = new TurbinesSelectAdapter(tb_list);
        RecyclerView recyclerView = dialog.findViewById(R.id.di_rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
        recyclerView.setAdapter(adapter);

        Button btn_close = dialog.findViewById(R.id.di_arview_closeButton);

        // NOTE : X 버튼 클릭 시 종료 이벤트 구현
        btn_close.setOnClickListener(v -> {
            dialog.dismiss();
        });
        // TODO : 선택 버튼 클릭 이벤트 구현 하기

        dialog.show();
    }

    private void showDialog_scoreInput() {
        Dialog dialog;
        dialog = new Dialog(MapActivity.this);
        dialog.setContentView(R.layout.dialog_score_input);

        Button btn_close = dialog.findViewById(R.id.dl_score_input_closeButton);

        // NOTE : X 버튼 클릭 시 종료 이벤트 구현
        btn_close.setOnClickListener(v -> {
            dialog.dismiss();
        });
        // TODO : 선택 버튼 클릭 이벤트 구현 하기

        dialog.show();
    }

    private void showBottomDialog_DD() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_dd);

        dialog.show();

        LinearLayout container = dialog.findViewById(R.id.di_dd_container);
        View closeBar = dialog.findViewById(R.id.di_dd_closeBar);

        EditText et_latitude = dialog.findViewById(R.id.dl_dd_et1);
        EditText et_longitude = dialog.findViewById(R.id.dl_dd_et2);

        Button btn_close = dialog.findViewById(R.id.di_dd_closeButton);
        Button btn_submit = dialog.findViewById(R.id.di_dd_submitButton);

        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);

        // 터치 이벤트 리스너
        container.setOnTouchListener((v, e) -> {
            // 터치할 때 색상 변경
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                closeBar.setBackgroundColor(Color.parseColor("#478A81"));
                // 터치가 끝날 때 색상 변경
            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                closeBar.setBackgroundColor(Color.parseColor("#60478A81"));
            }
            return true;
        });

        btn_close.setOnClickListener(v ->
        {
            dialog.dismiss();
        });

        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        // 다이얼로그가 닫히거나 숨겨질 때 색상 변경
                        closeBar.setBackgroundColor(Color.parseColor("#60478A81"));
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        // 다이얼로그가 확장되었을 때 색상 변경
                        closeBar.setBackgroundColor(Color.parseColor("#478A81"));
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                        // 다이얼로그가 드래그되거나 정착 중일 때 색상 변경
                        closeBar.setBackgroundColor(Color.parseColor("#478A81"));
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void showBottomDialog_DMS() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_dms);

        dialog.show();

        LinearLayout container = dialog.findViewById(R.id.di_dms_container);
        View closeBar = dialog.findViewById(R.id.di_dms_closeBar);

        EditText et_degrees1 = dialog.findViewById(R.id.dl_dms_et1);
        EditText et_degrees2 = dialog.findViewById(R.id.dl_dms_et2);

        EditText et_minutes1 = dialog.findViewById(R.id.dl_dms_et3);
        EditText et_minutes2 = dialog.findViewById(R.id.dl_dms_et4);

        EditText et_seconds1 = dialog.findViewById(R.id.dl_dms_et5);
        EditText et_seconds2 = dialog.findViewById(R.id.dl_dms_et6);

        Spinner sp_direction1 = dialog.findViewById(R.id.di_dms_spinner1);
        Spinner sp_direction2 = dialog.findViewById(R.id.di_dms_spinner2);

        Button btn_close = dialog.findViewById(R.id.di_dms_closeButton);
        Button btn_submit = dialog.findViewById(R.id.di_dms_submitButton);

        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);

        container.setOnClickListener(v -> {
            closeBar.setBackgroundColor(Color.parseColor("#478A81"));
        });

        btn_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        // 다이얼로그가 닫히거나 숨겨질 때 색상 변경
                        closeBar.setBackgroundColor(Color.parseColor("#60A87DB0"));
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        // 다이얼로그가 확장되었을 때 색상 변경
                        closeBar.setBackgroundColor(Color.parseColor("#A87DB0"));
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                        // 다이얼로그가 드래그되거나 정착 중일 때 색상 변경
                        closeBar.setBackgroundColor(Color.parseColor("#A87DB0"));
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        // 터치 이벤트 리스너
        container.setOnTouchListener((v, e) -> {
            // 터치할 때 색상 변경
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                closeBar.setBackgroundColor(Color.parseColor("#A87DB0"));
                // 터치가 끝날 때 색상 변경
            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                closeBar.setBackgroundColor(Color.parseColor("#60A87DB0"));
            }
            return true;
        });
    }
}

