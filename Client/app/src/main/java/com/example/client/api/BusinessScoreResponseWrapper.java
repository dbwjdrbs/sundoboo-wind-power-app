package com.example.client.api;

import java.util.List;

public class BusinessScoreResponseWrapper {
//    제이슨 data 필드를 포함하는 래퍼클래스 -> List<BusinessResponse> 포함 하는 필드를 가지고 있어야 함
    private MappingClass.BusinessScoreResponsePage data;

    public MappingClass.BusinessScoreResponsePage getData() {
        return data;
    }

    public void setData(MappingClass.BusinessScoreResponsePage data) {
        this.data = data;
    }
}

