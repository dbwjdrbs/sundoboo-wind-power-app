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
import com.unity3d.player.UnityPlayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class UnityPlayerActivity extends com.unity3d.player.UnityPlayerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        double objectLat = intent.getDoubleExtra("objectLat", 0.0);
        double objectLon = intent.getDoubleExtra("objectLon", 0.0);
        float direction = intent.getFloatExtra("direction", 0.0f);
        double distance = intent.getDoubleExtra("distance", 0.0);
        int modelNumber = intent.getIntExtra("number", 0);

        sendMessageToUnity(String.valueOf(objectLat), String.valueOf(objectLon), String.valueOf(direction), String.valueOf(distance), String.valueOf(modelNumber));
    }

    private void sendMessageToUnity(String objectLat, String objectLon, String direction, String distance, String modelNumber) {
        UnityPlayer.UnitySendMessage("Compass", "ReceiveDataFromAndroid", objectLat + "," + objectLon);
        UnityPlayer.UnitySendMessage("Turbine", "ReceiveDataFromAndroid", distance + "," + direction + "," + modelNumber + ",20");
    }
}
