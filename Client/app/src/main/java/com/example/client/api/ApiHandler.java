package com.example.client.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

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

    public void createBusiness(MappingClass.BusinessRequest request, ApiCallback<MappingClass.BusinessResponse2> callback) {
        Call<MappingClass.BusinessResponse2> call = apiService.createBusiness(request);
        call.enqueue(new Callback<MappingClass.BusinessResponse2>() {
            @Override
            public void onResponse(Call<MappingClass.BusinessResponse2> call, Response<MappingClass.BusinessResponse2> response) {
                if (response.isSuccessful()) {
                    Log.d("API Response", "Raw Response: " + response.raw());
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        callback.onError("Error: " + errorMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onError("Error: Unable to parse error body");
                    }
                }
            }

            @Override
            public void onFailure(Call<MappingClass.BusinessResponse2> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }

    public Call<Void> createLocation(MappingClass.LocationPostRequest request, final ApiCallback<Void> callback) {
        Call<Void> call = apiService.createLocation(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {

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
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    public void deleteLocationsByBusinessId(long businessId) {
        Call<Void> deletecall = apiService.deleteLocationsByBusinessId(businessId);
        deletecall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    System.out.println("Locations deleted successfully.");
                } else {
                    // 요청 실패
                    System.out.println("Failed to delete locations. Status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Failed to delete locations. Error: " + t.getMessage());
            }
        });
    }
    public void getBusinesses(int page, int size, String direction, String keyword, ApiCallback<List<MappingClass.BusinessResponse>> callback) {
        Call<BusinessResponseWrapper> call = apiService.getBusinesses(page, size, direction, keyword);
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

    public Call<Void> createScore(MappingClass.BusinessScorePost request, final ApiCallback<Void> callback) {
        Call<Void> call = apiService.createBusinessScore(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
        return call;
    }

    public void getLocations(long businessId, int page, int size, ApiCallback<List<MappingClass.LocationResponse>> callback) {
        Call<LocationResponseWrapper> call = apiService.getLocations(businessId, page, size);
        call.enqueue(new Callback<LocationResponseWrapper>() {
            @Override
            public void onResponse(Call<LocationResponseWrapper> call, Response<LocationResponseWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Log JSON 응답 확인
                    String responseBody = new Gson().toJson(response.body());
                    Log.d("API Response", responseBody);

                    List<MappingClass.LocationResponse> businessList = response.body().getData();
                    callback.onSuccess(businessList);
                } else {
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LocationResponseWrapper> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }

    public void getScores(long businessId, int page, int size, ApiCallback<MappingClass.BusinessScoreResponsePage> callback) {
        Call<MappingClass.BusinessScoreResponsePage> call = apiService.getBusinessScores(businessId, page, size);
        call.enqueue(new Callback<MappingClass.BusinessScoreResponsePage>() {
            @Override
            public void onResponse(Call<MappingClass.BusinessScoreResponsePage> call, Response<MappingClass.BusinessScoreResponsePage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Log JSON 응답 확인
                    String responseBody = new Gson().toJson(response.body());
                    Log.d("API Response", responseBody);

                    MappingClass.BusinessScoreResponsePage businessList = response.body();
                    callback.onSuccess(businessList);
                } else {
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MappingClass.BusinessScoreResponsePage> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }

    public void fetchBusinesses(int page, int size, String direction, final ApiCallback<BusinessResponseWrapper> callback) {
        Call<BusinessResponseWrapper> call = apiService.getBusinesses(page, size, direction);
        call.enqueue(new Callback<BusinessResponseWrapper>() {
            @Override
            public void onResponse(Call<BusinessResponseWrapper> call, Response<BusinessResponseWrapper> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
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
            public void onFailure(Call<BusinessResponseWrapper> call, Throwable t) {
                callback.onError("Failure : " + t.getMessage());
            }
        });
    }

    public void getDD(String latitude, String longitude, final ApiCallback<MappingClass.DdResponse> callback) {
        Call<MappingClass.DdResponse> call = apiService.getDD(latitude, longitude);
        call.enqueue(new Callback<MappingClass.DdResponse>() {
            @Override
            public void onResponse(Call<MappingClass.DdResponse> call, Response<MappingClass.DdResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        callback.onError("Error: " + errorMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onError("Error: Unable to parse error body");
                    }
                }
            }

            @Override
            public void onFailure(Call<MappingClass.DdResponse> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }


    public void patchLocation(MappingClass.LocationPatchRequest request, final ApiCallback<Void> callback) {
        Call<Void> call = apiService.patchLocation(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null); // 성공적으로 업데이트된 경우
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        callback.onError("Error: " + errorMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onError("Error: Unable to parse error body");
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }
}
