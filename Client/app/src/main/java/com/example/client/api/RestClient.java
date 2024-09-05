package com.example.client.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// API 호출을 위해 사용할 Retrofit 인스턴스 생성 - 직렬화, 역직렬화를 할 수 있는 환경을 설정
public class RestClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // lenient Gson 인스턴스 생성
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}