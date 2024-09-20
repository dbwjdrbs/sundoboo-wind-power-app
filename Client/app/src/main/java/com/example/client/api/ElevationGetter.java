package com.example.client.api;

import android.os.AsyncTask;
import android.util.Log;

import com.example.client.BuildConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ElevationGetter extends AsyncTask<Void, Void, Double> {
    private static final String API_KEY = BuildConfig.elevation_api_key;
    private static final String TAG = "ElevationGetter";

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
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            String urlString = String.format("https://maps.googleapis.com/maps/api/elevation/json?locations=%f,%f&key=%s", latitude, longitude, API_KEY);
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "HTTP error code: " + responseCode);
                return null;
            }

            JSONObject jsonResponse = new JSONObject(result.toString());
            JSONArray results = jsonResponse.getJSONArray("results");
            if (results.length() > 0) {
                JSONObject elevationData = results.getJSONObject(0);
                return elevationData.getDouble("elevation");
            } else {
                Log.e(TAG, "No results found in response");
                return null;
            }

        } catch (Exception e) {
            Log.e(TAG, "Error fetching elevation data", e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error closing reader", e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Double elevation) {
        if (listener != null) {
            listener.onElevationFetched(elevation);
        }
    }
}
