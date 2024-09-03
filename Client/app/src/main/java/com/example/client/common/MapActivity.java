package com.example.client.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.client.R;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

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

    }

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

        Log.d("MapActivity", "Latitude DMS: " + latDegrees + "° " + latMinutes + "' " + latSeconds + "''");
        Log.d("MapActivity", "Longitude DMS: " + lonDegrees + "° " + lonMinutes + "' " + lonSeconds + "''");
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



}


