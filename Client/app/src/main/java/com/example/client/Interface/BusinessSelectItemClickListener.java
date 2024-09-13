package com.example.client.Interface;

import com.example.client.data.BusinessData;

// 1. 인터페이스 정의
public interface BusinessSelectItemClickListener {
    void onBusinessItemClick(BusinessData businessData, int position);
}
