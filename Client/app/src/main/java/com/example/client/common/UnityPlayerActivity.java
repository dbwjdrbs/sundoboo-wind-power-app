package com.example.client.common;

import android.content.Intent;
import android.os.Bundle;

import com.unity3d.player.UnityPlayer;


public class UnityPlayerActivity extends com.unity3d.player.UnityPlayerActivity {
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

        sendMessageToUnity(String.valueOf(objectLat), String.valueOf(objectLon), String.valueOf(direction)
                , String.valueOf(modelNumber), myElevation, objElevation);
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
