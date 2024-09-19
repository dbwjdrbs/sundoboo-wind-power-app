package com.example.client.common;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.R;
import com.example.client.api.ApiCallback;
import com.example.client.api.ApiHandler;
import com.example.client.api.ApiService;
import com.example.client.api.MappingClass;
import com.example.client.api.RestClient;
import com.unity3d.player.UnityPlayer;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class UnityPlayerActivity extends com.unity3d.player.UnityPlayerActivity {

    private long locationId;
    private long businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        double objectLat = intent.getDoubleExtra("objectLat", 0.0);
        double objectLon = intent.getDoubleExtra("objectLon", 0.0);
        float direction = intent.getFloatExtra("direction", 0.0f);
        double scale = intent.getFloatExtra("scale", 0.0f);
        int modelNumber = intent.getIntExtra("number", 0);
        float elevation = intent.getFloatExtra("elevation", 0.0f);

        ApiService apiService = RestClient.getClient().create(ApiService.class);
        ApiHandler apiHandler = new ApiHandler(apiService, this);
        String stringLat = String.valueOf(objectLat);
        String stringLon = String.valueOf(objectLon);

        apiHandler.getDD(stringLat, stringLon, new ApiCallback<MappingClass.DdResponse>() {
            @Override
            public void onSuccess(MappingClass.DdResponse response) {
                Log.d("ApiHandler", "Location ID: " + response.getLocationId());
                Log.d("ApiHandler", "Business ID: " + response.getBusinessId());
                locationId = response.getLocationId();
                businessId = response.getBusinessId();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("ApiHandler", "Error: " + errorMessage);
            }
        });

        sendMessageToUnity(String.valueOf(objectLat), String.valueOf(objectLon), String.valueOf(direction),
                String.valueOf(scale), String.valueOf(modelNumber), elevation);
    }

    private void sendMessageToUnity(String objectLat, String objectLon, String direction, String scale, String modelNumber, float elevation) {
        UnityPlayer.UnitySendMessage("AndroidReceiveMessageManager", "ReceiveDataFromAndroidStudio",
                objectLat + "," + objectLon + "," + direction + "," + modelNumber + "," + scale + "," + elevation);
    }

    @Override
    public void onUnityPlayerQuitted() {
        super.onUnityPlayerQuitted();
        startActivity(new Intent(UnityPlayerActivity.this, MapActivity.class));
        ApiService apiService = RestClient.getClient().create(ApiService.class);
        ApiHandler apiHandler = new ApiHandler(apiService, this);
//        패치요청을 보낼 리퀘스트 바디 생성 -> 매개변수안에 set으로 넣어주시면 됩니다.
        MappingClass.LocationPatchRequest request = new MappingClass.LocationPatchRequest();
        request.setLocationId(locationId);
        request.setBusinessId(businessId);
//        MapActivity 에서 불러온 터빈아이디를 매개변수에 넣어주시면 됩니다.
        request.setTurbineId(1);
//        request.setCity();
//        request.setIsland();
        apiHandler.patchLocation(request, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void response) {
                // 성공적으로 업데이트된 경우 처리
                Log.d("ApiHandler", "Location updated successfully");
            }

            @Override
            public void onError(String errorMessage) {
                // 오류 처리
                Log.e("ApiHandler", "Error: " + errorMessage);
            }
        });
    }
}
