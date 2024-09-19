package com.example.client.api;

import java.util.List;

public class BusinessScoreResponseWrapper {
//    제이슨 data 필드를 포함하는 래퍼클래스 -> List<BusinessResponse> 포함 하는 필드를 가지고 있어야 함
    private List<MappingClass.BusinessScoreResponse> data;

    public List<MappingClass.BusinessScoreResponse> getData() {
        return data;
    }

    public void setData(List<MappingClass.BusinessScoreResponse> data) {
        this.data = data;
    }
}

