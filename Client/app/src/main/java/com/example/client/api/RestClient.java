package com.example.client.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// API 호출을 위해 사용할 Retrofit 인스턴스 생성 - 직렬화, 역직렬화를 할 수 있는 환경을 설정
public class RestClient {
    // 로컬 서버 주소 배포 여부에 따라 서버 주소로 변경해서 사용
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static Retrofit retrofit;

//    API 호출할 때 사용할 Retrofit 인스턴스 생성 메서드 - 싱글톤 패턴 사용
    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
//                   직렬, 역직렬화를 해줄 컨터버 팩토리 생성 및 사용
//                   직렬 JAVA 객체를 JSON 형식으로 변환하여 서버로 전송
//                   역직렬 : 서버로부터 받은 JSON 데이터를 JAVA 객체로 변환
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
