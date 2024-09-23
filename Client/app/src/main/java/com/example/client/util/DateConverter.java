package com.example.client.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    public static String convertTimestampString(String timestampString) throws ParseException {
        // SQLite에서 받은 문자열을 Date 객체로 변환
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // SQLite 기본 형식
        Date date = inputFormat.parse(timestampString);

        // 원하는 형식으로 포맷
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy년 MM월 dd일 a HH시 mm분");
        return outputFormat.format(date); // 포맷된 문자열 반환
    }

}
