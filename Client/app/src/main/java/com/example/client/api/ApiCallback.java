package com.example.client.api;

// api핸들러에서 사용할 커스텀 콜백
public interface ApiCallback<T>{

//    성공 했을 때 해당 타입의 응답 객체를 반환
    void onSuccess (T response);
//    실패 했을 때 에러 메세지 출력
    void onError(String errorMessage);
}
