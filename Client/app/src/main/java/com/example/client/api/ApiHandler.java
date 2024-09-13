package com.example.client.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiHandler {
    private ApiService apiService;
    private Context context;

    public ApiHandler(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    public Call<MappingClass.EmptyResponse> createBusiness(MappingClass.BusinessRequest request, final ApiCallback<Void> callback) {
        Call<MappingClass.EmptyResponse> call = apiService.createBusiness(request);
        call.enqueue(new Callback<MappingClass.EmptyResponse>() {
            @Override
            public void onResponse(Call<MappingClass.EmptyResponse> call, Response<MappingClass.EmptyResponse> response) {
                if (response.isSuccessful()) {

                    callback.onSuccess(null);
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        callback.onError("Error : " + errorMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onError("Error : Unable to parse error body");
                    }
                }
            }

            @Override
            public void onFailure(Call<MappingClass.EmptyResponse> call, Throwable t) {
                callback.onError("Failure : " + t.getMessage());
            }
        });
        return call;
    }

    public void deleteBusiness(long businessId) {
        Call<Void> deleteCall = apiService.deleteBusiness(businessId);
        deleteCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Business deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete business", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getBusinesses(int page, int size, String direction, ApiCallback<List<MappingClass.BusinessResponse>> callback) {
        Call<BusinessResponseWrapper> call = apiService.getBusinesses(page, size, direction);
        call.enqueue(new Callback<BusinessResponseWrapper>() {
            @Override
            public void onResponse(Call<BusinessResponseWrapper> call, Response<BusinessResponseWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Log JSON 응답 확인
                    String responseBody = new Gson().toJson(response.body());
                    Log.d("API Response", responseBody);

                    List<MappingClass.BusinessResponse> businessList = response.body().getData();
                    callback.onSuccess(businessList);
                } else {
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BusinessResponseWrapper> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }
}