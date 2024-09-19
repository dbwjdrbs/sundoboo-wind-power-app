package com.example.client.api;

import java.util.List;

public class LocationResponseWrapper {
//    제이슨 data 필드를 포함하는 래퍼클래스 -> List<BusinessResponse> 포함 하는 필드를 가지고 있어야 함
    private List<MappingClass.LocationResponse> data;

    public List<MappingClass.LocationResponse> getData() {
        return data;
    }

    public void setData(List<MappingClass.LocationResponse> data) {
        this.data = data;
    }
}

