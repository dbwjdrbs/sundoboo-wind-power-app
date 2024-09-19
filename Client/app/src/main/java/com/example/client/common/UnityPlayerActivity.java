package com.example.client.common;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

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
        int modelNumber = intent.getIntExtra("number", 0);
        float myElevation = intent.getFloatExtra("myElevation", 0.0f);
        float objElevation = intent.getFloatExtra("objElevation", 0.0f);

        ApiService apiService = RestClient.getClient().create(ApiService.class);
        ApiHandler apiHandler = new ApiHandler(apiService, this);
        String stringLat = String.valueOf(objectLat);
        String stringLon = String.valueOf(objectLon);


        sendMessageToUnity(String.valueOf(objectLat), String.valueOf(objectLon), String.valueOf(direction),
                String.valueOf(modelNumber), myElevation, objElevation);

    }

    private void sendMessageToUnity(String objectLat, String objectLon, String direction, String modelNumber, float myElevation, float objElevation) {
        String maxScale = "50";
        String minScale = "5";

        UnityPlayer.UnitySendMessage("AndroidReceiveMessageManager", "ReceiveDataFromAndroidStudio",
                objectLat + "," + objectLon + "," + direction + "," + modelNumber + "," + maxScale + "," + minScale + "," + myElevation + "," + objElevation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onUnityPlayerQuitted() {
        super.onUnityPlayerQuitted();
        onDestroy();
        startActivity(new Intent(UnityPlayerActivity.this, MapActivity.class));
    }
}