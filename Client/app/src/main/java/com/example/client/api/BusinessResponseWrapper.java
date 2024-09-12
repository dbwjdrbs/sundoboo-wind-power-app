package com.example.client.api;

import java.util.List;

public class BusinessResponseWrapper {
//    제이슨 data 필드를 포함하는 래퍼클래스 -> List<BusinessResponse> 포함 하는 필드를 가지고 있어야 함
    private List<MappingClass.BusinessResponse> data;

    public List<MappingClass.BusinessResponse> getData() {
        return data;
    }

    public void setData(List<MappingClass.BusinessResponse> data) {
        this.data = data;
    }
}

