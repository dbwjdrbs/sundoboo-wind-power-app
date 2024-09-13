package com.example.client.util;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ElevationGetter extends AsyncTask<Void, Void, Double> {
    private static final String API_KEY = "AIzaSyBNh4lqp8pK088v8ZO3EEMSYAkJrRdmZM4"; // 여기에 자신의 API 키를 입력하세요
    private final double latitude;
    private final double longitude;
    private final OnElevationFetchedListener listener;

    public interface OnElevationFetchedListener {
        void onElevationFetched(Double elevation);
    }

    public ElevationGetter(double latitude, double longitude, OnElevationFetchedListener listener) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.listener = listener;
    }

    @Override
    protected Double doInBackground(Void... voids) {
        String response = null;
        try {
            String urlString = String.format(
                    "https://maps.googleapis.com/maps/api/elevation/json?locations=%f,%f&key=%s",
                    latitude, longitude, API_KEY
            );
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            connection.disconnect();
            Log.d("ElevationFetcher", "Response: " + result.toString());
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.e("ElevationFetcher", "HTTP error code: " + responseCode);
                return null;
            }

            JSONObject jsonResponse = new JSONObject(result.toString());
            JSONArray results = jsonResponse.getJSONArray("results");
            JSONObject elevationData = results.getJSONObject(0);
            return elevationData.getDouble("elevation");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Double elevation) {
        if (listener != null) {
            listener.onElevationFetched(elevation);
        }
    }
}
