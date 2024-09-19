package com.example.client.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.R;
import com.example.client.adapter.ScoreListAdapter;
import com.example.client.adapter.TurbinesSelectAdapter;
import com.example.client.api.ApiCallback;
import com.example.client.api.ApiHandler;
import com.example.client.api.ApiService;
import com.example.client.api.MappingClass;
import com.example.client.api.RestClient;
import com.example.client.data.ScoreData;
import com.example.client.data.TurbinesData;
import com.example.client.util.ElevationGetter;
import com.example.client.util.GeoJsonFeatureCollection;
import com.example.client.util.MessageDialog;
import com.example.client.util.ProgressDialog;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, TurbinesSelectAdapter.OnItemClickListener {
    private GoogleMap mMap;
    private Marker mMarker;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean isCreateMarker = false;
    private boolean isOnClickMarker = false;
    private boolean isVisibleRegulatedArea = true; // 규제 지역
    private double[] currentMarkerPositions = new double[2];
    private double[] currentMyPositions = new double[2];
    private MessageDialog messageDialog = new MessageDialog();
    private List<Marker> markerList = new ArrayList<>();
    private List<Polygon> polygons = new ArrayList<>();
    private long businessId;


    // INFO : Unity 연동을 위한 것들
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private double myElevation;
    private double objElevation;
    private ProgressDialog customProgressDialog;

    // ======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        businessId = intent.getLongExtra("businessId", 0);

        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customProgressDialog.setCancelable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000); // 1초 대기
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        customProgressDialog.dismiss();
                    }
                });
            }
        }).start();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // SupportMapFragment를 찾아서 지도가 준비되었을 때 콜백을 받을 수 있도록 설정
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // NOTE : 버튼 온클릭 리스너 선언
        findViewById(R.id.btn_posSelect).setOnClickListener(this);
        findViewById(R.id.btn_posInput).setOnClickListener(this);
        findViewById(R.id.btn_arView).setOnClickListener(this);
        findViewById(R.id.btn_scoreInput).setOnClickListener(this);
        findViewById(R.id.btn_scoreList).setOnClickListener(this);
        findViewById(R.id.btn_deleteMarker).setOnClickListener(this);
        findViewById(R.id.btn_regulatedArea).setOnClickListener(this);
    }

    // ================================================================ GoogleMap
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;  // GoogleMap 객체를 초기화

        ApiService apiService = RestClient.getClient().create(ApiService.class);
        ApiHandler apiHandler = new ApiHandler(apiService, this);
        apiHandler.getLocations(businessId, 1, 30, new ApiCallback<List<MappingClass.LocationResponse>>() {
            @Override
            public void onSuccess(List<MappingClass.LocationResponse> response) {
                for (MappingClass.LocationResponse locaResponse : response) {
                    double latitude = Double.valueOf(locaResponse.getLatitude());
                    double longitude = Double.valueOf(locaResponse.getLongitude());

                    LatLng latLng = new LatLng(latitude, longitude);
                    initialMarkers(latLng);
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

        if (mMap != null) {
            try {
                InputStream is = getResources().openRawResource(R.raw.fss_a);
                GeoJsonFeatureCollection featureCollection = new Gson().fromJson(new InputStreamReader(is), GeoJsonFeatureCollection.class);
                for (GeoJsonFeatureCollection.GeoJsonFeature feature : featureCollection.features) {
                    if ("MultiPolygon".equals(feature.geometry.type)) {
                        for (List<List<List<Double>>> polygon : feature.geometry.coordinates) {
                            List<LatLng> polygonPoints = new ArrayList<>();
                            for (List<Double> point : polygon.get(0)) { // 첫 번째 폴리곤의 꼭지점만 가져옴
                                double lng = point.get(0);
                                double lat = point.get(1);
                                polygonPoints.add(new LatLng(lat, lng));
                            }
                            Polygon poly = mMap.addPolygon(new PolygonOptions()
                                    .addAll(polygonPoints)
                                    .strokeColor(0xFFFF7979)
                                    .fillColor(0x7FFF0000));
                            polygons.add(poly); // 폴리곤을 리스트에 추가
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
        // 마커 스타일

        // 지도 클릭 리스너 설정
        mMap.setOnMapClickListener(latLng -> {
            if (isCreateMarker) {
                setMarker(latLng);
            }
        });

        // INFO : 마커 온클릭
        googleMap.setOnMarkerClickListener(marker -> {
            isOnClickMarker = true;
            double latitude = marker.getPosition().latitude;
            double longitude = marker.getPosition().longitude;
            currentMarkerPositions[0] = latitude;
            currentMarkerPositions[1] = longitude;

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            // NOTE : 고도 API 호출
                            new ElevationGetter(location.getLatitude(), location.getLongitude(), elevation -> {
                                if (elevation != null) {
                                    myElevation = elevation;
                                }
                            }).execute();
                        }
                    });

            new ElevationGetter(currentMarkerPositions[0], currentMarkerPositions[1], elevation -> {
                if (elevation != null) {
                    objElevation = elevation;
                }
            }).execute();

            // NOTE : true -> 비활성화 false -> 기본 동작이 실행됨.
            return false;
        });
        customProgressDialog.dismiss();
    }

    private void initialMarkers(LatLng latLng) {
        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE); // NOTE : 구글 맵 마커 스타일

        mMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(icon)  // 아이콘 설정
                .alpha(0.8f)); // 마커의 투명도 설정 (0.0f ~ 1.0f)
        // NOTE : 클릭한 위치의 위도, 경도 정보를 DB에 저장
        markerList.add(mMarker);
    }

    // INFO : 마커 생성 메서드
    private void setMarker(LatLng latLng) {
        boolean isRegulatedArea = false;

        for (Polygon polygon : polygons) {
            List<LatLng> polygonPoints = polygon.getPoints();
            if (FindRegulatedArea(latLng, polygonPoints)) {
                isRegulatedArea = true;
                break;
            }
        }

        if (!isRegulatedArea) { // 규제 지역 바깥일 경우.
            // NOTE : 마커 지역 특정 코드
            Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
            try {
                // 위치에 대한 주소 정보 가져오기
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    String adminArea = address.getAdminArea(); // 전체 주소
                    String local = address.getLocality();
                    // 예시: 서울특별시 강남구 테헤란로 123

                    // 주소를 Toast로 표시
                } else {
                    // 주소를 찾지 못했을 경우
                    Toast.makeText(MapActivity.this, "주소를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE); // NOTE : 구글 맵 마커 스타일

            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(icon)  // 아이콘 설정
                    .alpha(0.8f)); // 마커의 투명도 설정 (0.0f ~ 1.0f)
            // NOTE : 클릭한 위치의 위도, 경도 정보를 DB에 저장
            markerList.add(mMarker);
            saveLocationToDatabase(latLng);

//        이때 로케이션 post 요청을 보내야함 -> 해당 비즈니스 ID를 입력하여 로케이션 생성하게 만들 어둠
//        위치는 결국 풍력 발전기를 선택하고 나서 줘야 넣어야 하므로 일단 생성만 해둔다.
//        fix 마커를 생성할 떄 위도 경도 까지 넣어주기로 변경
//        TODO : setBusinessId 안에 해당 사업을 눌렀을 때 가져온 BusinessId를 매개 변수에 넣어줘야한다.
            MappingClass.LocationPostRequest request = new MappingClass.LocationPostRequest();
            request.setBusinessId(1);
            request.setTurbineId(1);
            String latitude = String.valueOf(latLng.latitude);
            String longitude = String.valueOf(latLng.longitude);
            request.setLatitude(latitude);
            request.setLongitude(longitude);

            ApiService apiService = RestClient.getClient().create(ApiService.class);
            ApiHandler apiHandler = new ApiHandler(apiService, this);

            apiHandler.createLocation(request, new ApiCallback<Void>() {

                @Override
                public void onSuccess(Void response) {

                }

                @Override
                public void onError(String errorMessage) {

                }
            });

            messageDialog.simpleCompleteDialog("건설 예정지 등록이 완료되었습니다.", this);
        } else {
            // 에러 메시지
            messageDialog.simpleErrorDialog("건설 예정지 생성이 불가능한 지역입니다.", this);
        }
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
                        // 카메라를 현재 위치로 이동 및 확대
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
//        Toast.makeText(this, "Latitude DMS: " + latDegrees + "° " + latMinutes + "' " + latSeconds + "''", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Longitude DMS: " + lonDegrees + "° " + lonMinutes + "' " + lonSeconds + "''", Toast.LENGTH_SHORT).show();
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

    // INFO : GoeJSON 로드 (규제지역)
    private void togglePolygonsVisibility() {
        isVisibleRegulatedArea = !isVisibleRegulatedArea;

        for (Polygon polygon : polygons) {
            polygon.setVisible(isVisibleRegulatedArea);
        }
    }

    private boolean FindRegulatedArea(LatLng point, List<LatLng> polygonPoints) {
        int numPoints = polygonPoints.size();
        boolean inside = false;

        for (int i = 0, j = numPoints - 1; i < numPoints; j = i++) {
            LatLng pi = polygonPoints.get(i);
            LatLng pj = polygonPoints.get(j);

            if ((pi.latitude > point.latitude) != (pj.latitude > point.latitude) &&
                    (point.longitude < (pj.longitude - pi.longitude) * (point.latitude - pi.latitude) / (pj.latitude - pi.latitude) + pi.longitude)) {
                inside = !inside;
            }
        }
        return inside;
    }

// ================================================================================

    // INFO : 버튼 온클릭 이벤트 정의 메서드
    @Override
    public void onClick(View v) {
        Button btn_posSelect = findViewById(R.id.btn_posSelect);
        // INFO : 좌표 선택 버튼
        if (v.getId() == R.id.btn_posSelect) {
            isCreateMarker = !isCreateMarker;
            if (isCreateMarker) {
                btn_posSelect.setText("선택 해제");
                btn_posSelect.setTextColor(Color.parseColor("#D1180B"));
            } else {
                btn_posSelect.setText("좌표 선택");
                btn_posSelect.setTextColor(Color.parseColor("#000000"));
            }
        }

        // INFO : 좌표 입력 버튼
        if (v.getId() == R.id.btn_posInput) {
            showDialog_posInput();
        }

        // INFO : AR 확인 버튼
        if (v.getId() == R.id.btn_arView) {
            if (isOnClickMarker) {
                showDialog_arView();
            } else {
                messageDialog.simpleErrorDialog("마커를 선택해주세요.", this);
            }
        }

        // INFO : 점수 입력 버튼
        if (v.getId() == R.id.btn_scoreInput) {
            showDialog_scoreInput();
        }

        // INFO : 점수 목록 버튼
        if (v.getId() == R.id.btn_scoreList) {
            showDialog_scoreList();
        }

        // INFO : 규제 지역 버튼
        if (v.getId() == R.id.btn_regulatedArea) {
            togglePolygonsVisibility();
        }

        // INFO : 마커 삭제 구현
        if (v.getId() == R.id.btn_deleteMarker) {
            if (mMarker != null) {
                for (Marker marker : markerList) {
                    marker.remove();
                }
//                TODO : 로케이션 삭제 로직 DeleteLocationsByBusinessId() 해당 사업 id에 있는 로케이션들을 삭제

                MappingClass.DeleteLocationsByBusinessId request = new MappingClass.DeleteLocationsByBusinessId();
                request.setBusinessId(1);
                ApiService apiService = RestClient.getClient().create(ApiService.class);
                ApiHandler apiHandler = new ApiHandler(apiService, this);
                apiHandler.deleteLocationsByBusinessId(request.getBusinessId());

                markerList.clear();
                mMarker = null;
                isOnClickMarker = false;
                String[] markers = {String.valueOf(currentMarkerPositions[0]), String.valueOf(currentMarkerPositions[1])};
                Arrays.fill(markers, null);

                messageDialog.simpleCompleteDialog("마커가 초기화 되었습니다.", this);
            } else {
                messageDialog.simpleErrorDialog("생성된 마커가 없습니다.", this);
            }
        }
    }

    // INFO : 좌표 입력 버튼 클릭 시, 나오는 팝업창
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

    // INFO : AR 확인 버튼 클릭 시, 나오는 팝업창
    private void showDialog_arView() {
        Dialog dialog;
        dialog = new Dialog(MapActivity.this);
        dialog.setContentView(R.layout.dialog_arview);

        // NOTE : 풍력 발전기 모델 더미 데이터
        TurbinesData data1 = new TurbinesData("두산 WinDS3300", "Doosan WinDS3300");
        TurbinesData data2 = new TurbinesData("두산 WinDS5500", "Doosan WinDS5500");
        TurbinesData data3 = new TurbinesData("두산 WinDS205-8MW", "WinDS205-8MW");

        tb_list = new ArrayList<>();
        tb_list.add(data1);
        tb_list.add(data2);
        tb_list.add(data3);

        // NOTE : 리사이클러뷰 어뎁터 정의
        TurbinesSelectAdapter adapter = new TurbinesSelectAdapter(tb_list, this);
        RecyclerView recyclerView = dialog.findViewById(R.id.di_rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
        recyclerView.setAdapter(adapter);

        Button btn_close = dialog.findViewById(R.id.dl_arview_closeButton);
//        Button btn_select = dialog.findViewById(R.id.di_arview_selectButton);

        // NOTE : X 버튼 클릭 시 종료 이벤트 구현
        btn_close.setOnClickListener(v -> {
            dialog.dismiss();
        });


        dialog.show();
    }

    // INFO : 점수 입력 버튼 클릭 시 나오는 팝업 창
    private void showDialog_scoreInput() {
        Dialog dialog;
        dialog = new Dialog(MapActivity.this);
        dialog.setContentView(R.layout.dialog_score_input);

        EditText et_title = dialog.findViewById(R.id.dl_score_input_et1);
        EditText et_observerName = dialog.findViewById(R.id.dl_score_input_et2);

        Button btn_close = dialog.findViewById(R.id.dl_score_input_closeButton);
        Button btn_submit = dialog.findViewById(R.id.dl_score_input_submitButton);

        SeekBar seekBar1 = dialog.findViewById(R.id.seekBar_score1);
        SeekBar seekBar2 = dialog.findViewById(R.id.seekBar_score2);
        SeekBar seekBar3 = dialog.findViewById(R.id.seekBar_score3);
        SeekBar seekBar4 = dialog.findViewById(R.id.seekBar_score4);

        // INFO : X 버튼 클릭 이벤트
        btn_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // INFO : 점수 등록 버튼 클릭 이벤트
        btn_submit.setOnClickListener(v -> {
            if (et_title.getText().toString().isEmpty()) {
                messageDialog.simpleErrorDialog("명칭을 작성해주세요.", this);
            } else if (et_observerName.getText().toString().isEmpty()) {
                messageDialog.simpleErrorDialog("관찰자명을 작성해주세요.", this);
            } else {
                String title = et_title.getText().toString();
                int score1 = seekBar1.getProgress() + 1;
                int score2 = seekBar2.getProgress() + 1;
                int score3 = seekBar3.getProgress() + 1;
                int score4 = seekBar4.getProgress() + 1;
                String observer = et_observerName.getText().toString();

                MappingClass.BusinessScorePost request = new MappingClass.BusinessScorePost(businessId, title, observer, score1, score2, score3, score4);

                ApiService apiService = RestClient.getClient().create(ApiService.class);
                ApiHandler apiHandler = new ApiHandler(apiService, this);
                apiHandler.createScore(request, new ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void response) {
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
                messageDialog.simpleCompleteDialog("점수 등록이 완료되었습니다.", this);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private ArrayList<ScoreData> sd_list;

    // INFO : 점수 입력 **목록** 버튼 클릭시 생기는 팝업창
    private void showDialog_scoreList() {
        Dialog dialog;
        dialog = new Dialog(MapActivity.this);
        dialog.setContentView(R.layout.dialog_scorelist);

        sd_list = new ArrayList<>();

        // NOTE : 리사이클러뷰 어뎁터 정의
        ScoreListAdapter adapter = new ScoreListAdapter(sd_list);
        RecyclerView recyclerView = dialog.findViewById(R.id.dl_rv_scorelist);

        recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
        recyclerView.setAdapter(adapter);

        TextView tv_left_button = dialog.findViewById(R.id.tv_left_scoreList);
        TextView tv_right_button = dialog.findViewById(R.id.tv_right_scoreList);
        TextView tv_page_number = dialog.findViewById(R.id.tv_page_number);

        int page = Integer.parseInt(tv_page_number.getText().toString());
        final MappingClass.PageInfo pageInfo = requestScoreList(page, adapter);

        tv_left_button.setOnClickListener(view -> {
            int num = Integer.parseInt(tv_page_number.getText().toString());
            if (num != 1) {
                num -= 1;
                pageInfo.setPageInfo(requestScoreList(num, adapter));
            }
            tv_page_number.setText(String.valueOf(num));
        });

        tv_right_button.setOnClickListener(view -> {
            int num = Integer.parseInt(tv_page_number.getText().toString());
            if (num != 0 && num < pageInfo.getTotalPages()) {
                num += 1;
                pageInfo.setPageInfo(requestScoreList(num, adapter));
            }
            tv_page_number.setText(String.valueOf(num));
        });


        Button btn_close = dialog.findViewById(R.id.dl_scorelist_closeButton);

        // NOTE : X 버튼 클릭 시 종료 이벤트 구현
        btn_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // TODO : 선택 버튼 클릭 이벤트 구현 하기

        dialog.show();
    }

    private MappingClass.PageInfo requestScoreList(int page, ScoreListAdapter adapter) {
        ApiService apiService = RestClient.getClient().create(ApiService.class);
        ApiHandler apiHandler = new ApiHandler(apiService, this);
        final MappingClass.PageInfo pageInfo = new MappingClass.PageInfo();
        apiHandler.getScores(businessId, page, 5, new ApiCallback<MappingClass.BusinessScoreResponsePage>() {
            @Override
            public void onSuccess(MappingClass.BusinessScoreResponsePage response) {
                sd_list.clear();
                pageInfo.setPageInfo(response.getPageInfo());
                for (MappingClass.BusinessScoreResponse bsResponse : response.getData()) {
                    String title = bsResponse.getBusinessScoreTitle();
                    String observer = bsResponse.getObserverName();
                    String createAt = bsResponse.getCreatedAt();
                    int score1 = bsResponse.getScoreList1() - 1;
                    int score2 = bsResponse.getScoreList2() - 1;
                    int score3 = bsResponse.getScoreList3() - 1;
                    int score4 = bsResponse.getScoreList4() - 1;

                    sd_list.add(new ScoreData(title, observer, createAt, score1, score2, score3, score4));
                }
                adapter.pageChange(sd_list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

        return pageInfo;
    }

    // INFO : 좌표 입력 -> DD 클릭시 나오는 하단 팝업창
    private void showBottomDialog_DD() {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.DialogStyle);
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

        btn_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // INFO : 완료 버튼 클릭 이벤트
        btn_submit.setOnClickListener(v -> {
            // NOTE : EditText 부분이 비었을 경우를 대비한 예외 처리.
            if (et_latitude.getText().toString().isEmpty()) {
                messageDialog.simpleErrorDialog("Latitude를 작성해주세요.", this);
            } else if (et_longitude.getText().toString().isEmpty()) {
                messageDialog.simpleErrorDialog("Longitude를 작성해주세요.", this);
            } else {
                setMarker(new LatLng(Double.parseDouble(String.valueOf(et_latitude.getText())), Double.parseDouble(String.valueOf(et_longitude.getText()))));
                dialog.dismiss();
            }
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

    // INFO : 좌표 입력 -> DMS 클릭시 나오는 하단 팝업창
    private void showBottomDialog_DMS() {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.dialog_dms);

        dialog.show();

        LinearLayout container = dialog.findViewById(R.id.di_dms_container);
        View closeBar = dialog.findViewById(R.id.di_dms_closeBar);

        EditText et_degrees_lat = dialog.findViewById(R.id.dl_dms_et1);
        EditText et_degrees_lon = dialog.findViewById(R.id.dl_dms_et2);

        EditText et_minutes_lat = dialog.findViewById(R.id.dl_dms_et3);
        EditText et_minutes_lon = dialog.findViewById(R.id.dl_dms_et4);

        EditText et_seconds_lat = dialog.findViewById(R.id.dl_dms_et5);
        EditText et_seconds_lon = dialog.findViewById(R.id.dl_dms_et6);

        Spinner sp_direction_lat = dialog.findViewById(R.id.di_dms_spinner1);
        Spinner sp_direction_lon = dialog.findViewById(R.id.di_dms_spinner2);

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

        // INFO : 좌표 등록 버튼 클릭 이벤트
        btn_submit.setOnClickListener(v -> {
            if (et_degrees_lat.getText().toString().isEmpty()) {
                messageDialog.simpleErrorDialog("Latitude의 Degrees 부분을 작성해주세요.", this);
            } else if (et_degrees_lon.getText().toString().isEmpty()) {
                messageDialog.simpleErrorDialog("Longitude의 Degrees 부분을 작성해주세요.", this);
            } else if (et_minutes_lat.getText().toString().isEmpty()) {
                messageDialog.simpleErrorDialog("Latitude의 Minutes 부분을 작성해주세요.", this);
            } else if (et_minutes_lon.getText().toString().isEmpty()) {
                messageDialog.simpleErrorDialog("Longitude의 Minutes 부분을 작성해주세요.", this);
            } else if (et_seconds_lat.getText().toString().isEmpty()) {
                messageDialog.simpleErrorDialog("Latitude의 Seconds 부분을 작성해주세요.", this);
            } else if (et_seconds_lon.getText().toString().isEmpty()) {
                messageDialog.simpleErrorDialog("Longitude의 Seconds 부분을 작성해주세요.", this);
            } else {
                double latDecimal = dmsToDecimal(editTextStringToInt(et_degrees_lat), editTextStringToInt(et_minutes_lat), editTextStringToDouble(et_seconds_lat), sp_direction_lat.getSelectedItem().toString());
                double lonDecimal = dmsToDecimal(editTextStringToInt(et_degrees_lon), editTextStringToInt(et_minutes_lon), editTextStringToDouble(et_seconds_lon), sp_direction_lon.getSelectedItem().toString());
                setMarker(new LatLng(latDecimal, lonDecimal));
                dialog.dismiss();
            }
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

    // INFO : dms to Decimal 변환
    private double dmsToDecimal(int degrees, int minutes, double seconds, String direction) {
        // 도, 분, 초를 소수점 형식으로 변환
        double decimal = degrees + (minutes / 60.0) + (seconds / 3600.0);
        // 방향에 따라 부호를 조정
        if (direction.equals("S") || direction.equals("W")) {
            decimal = -decimal;
        }

        return decimal;
    }

    // INFO : editText to Int 변환
    private int editTextStringToInt(EditText editText) {
        return Integer.parseInt(String.valueOf(editText.getText()));
    }

    // INFO : editText to Double 변환
    private double editTextStringToDouble(EditText editText) {
        return Double.parseDouble(String.valueOf(editText.getText()));
    }

    // INFO : AR뷰 보기
    // 0 1 2 3 -> +1해서 넣어 줘야 터빈 아이디가 댐
    @Override
    public void onSelectModel(int position, int direction) {
        getLocationAndSendToUnity(position, direction);
    }


    private void getLocationAndSendToUnity(int modelNumber, int direction) { // NOTE : 나중에 position 별로 터빈 나누기!!!
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        // 물체 위치
        double objectLatitude = currentMarkerPositions[0];
        double objectLongitude = currentMarkerPositions[1];
        launchUnityAppWithData(objectLatitude, objectLongitude, direction, modelNumber, (float) myElevation, (float) objElevation);
    }

    // INFO : Unity에 데이터 전달
    private void launchUnityAppWithData(double objectLat, double objectLon, float direction, int modelNumber, float myElevation, float objElevation) {
        Intent intent = new Intent(MapActivity.this, com.example.client.common.UnityPlayerActivity.class);
        intent.putExtra("objectLat", objectLat);
        intent.putExtra("objectLon", objectLon);
        intent.putExtra("direction", direction);
        intent.putExtra("number", modelNumber);
        intent.putExtra("myElevation", myElevation);
        intent.putExtra("objElevation", objElevation);

        startActivity(intent);
    }
}