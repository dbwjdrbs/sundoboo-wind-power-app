package com.example.client.localdb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.client.data.LocationData;

import java.util.ArrayList;
import java.util.List;

public class DBControl {
    DBHelper helper;
    SQLiteDatabase db;

    public DBControl(DBHelper helper) {
        this.helper = helper;
    }

    public List<String[]> select(String table, String... column) {
        db = helper.getReadableDatabase();
        List<String[]> resultList = new ArrayList<>();

        // 모든 컬럼을 조회합니다.
        Cursor c = db.query(table, column, null, null, null, null, null);

        // 커서의 각 행을 반복하면서 데이터를 수집합니다.
        while (c.moveToNext()) {
            int columnCount = c.getColumnCount();
            String[] rowValues = new String[columnCount];

            for (int i = 0; i < columnCount; i++) {
                rowValues[i] = c.getString(i);
            }

            resultList.add(rowValues);
        }
        c.close();
        for (String[] str : resultList) {
            Log.d("비즈니스 조회", str[0]);
        }

        return resultList;
    }


    public List<String[]> select(String table, String keyword, String[] columns) {
        db = helper.getReadableDatabase();
        List<String[]> resultList = new ArrayList<>();

        String selection = null;
        String[] selectionArgs = null;

        // keyword가 유효한 경우에만 검색 조건 설정
        if (keyword != null && !keyword.isEmpty()) {
            selection = "business_title LIKE ?";
            selectionArgs = new String[]{"%" + keyword + "%"};
        }

        Cursor c = null;
        try {
            // 쿼리 실행
            c = db.query(table, columns, selection, selectionArgs, null, null, null);

            // 커서의 각 행을 반복하면서 데이터를 수집합니다.
            while (c.moveToNext()) {
                int columnCount = c.getColumnCount();
                String[] rowValues = new String[columnCount];

                for (int i = 0; i < columnCount; i++) {
                    rowValues[i] = c.getString(i);
                }

                resultList.add(rowValues);
            }
        } catch (Exception e) {
            Log.e("DB Error", "Error executing query: " + e.getMessage(), e);
        } finally {
            // 커서가 null이 아닐 경우 닫기
            if (c != null) {
                c.close();
            }
        }

        return resultList;
    }

    public List<LocationData> selectByCoordinates(String table, double latitude, double longitude) {
        db = helper.getReadableDatabase();
        List<LocationData> resultList = new ArrayList<>();

        // 쿼리 조건
        String selection = "latitude = ? AND longitude = ?";
        String[] selectionArgs = new String[]{String.valueOf(latitude), String.valueOf(longitude)};

        Cursor c = null;
        try {
            // 쿼리 실행
            c = db.query(table, null, selection, selectionArgs, null, null, null);

            // 커서의 각 행을 반복하면서 데이터를 수집합니다.
            while (c.moveToNext()) {
                int locationIdIndex = c.getColumnIndex("location_id");
                int businessIdIndex = c.getColumnIndex("business_id");
                int turbineIdIndex = c.getColumnIndex("turbine_id");
                int latitudeIndex = c.getColumnIndex("latitude");
                int longitudeIndex = c.getColumnIndex("longitude");
                int cityIndex = c.getColumnIndex("city");
                int islandIndex = c.getColumnIndex("island");
                int createdAtIndex = c.getColumnIndex("created_at");

                // 인덱스가 -1이 아닌지 확인
                if (locationIdIndex == -1 || businessIdIndex == -1 || turbineIdIndex == -1 ||
                        latitudeIndex == -1 || longitudeIndex == -1 || cityIndex == -1 ||
                        islandIndex == -1 || createdAtIndex == -1) {
                    Log.e("DB Error", "One or more columns not found in the database.");
                    continue; // 컬럼이 없을 경우 다음 행으로 넘어감
                }

                // 데이터 가져오기
                long locationId = c.getLong(locationIdIndex);
                long businessId = c.getLong(businessIdIndex);
                long turbineId = c.getLong(turbineIdIndex);
                double lat = c.getDouble(latitudeIndex);
                double lon = c.getDouble(longitudeIndex);
                String city = c.getString(cityIndex);
                String island = c.getString(islandIndex);
                String createdAt = c.getString(createdAtIndex);

                // LocationData 객체 생성 및 리스트에 추가
                resultList.add(new LocationData(locationId, businessId, turbineId, lat, lon, city, island, createdAt));
            }
        } catch (Exception e) {
            Log.e("DB Error", "Error executing query: " + e.getMessage(), e);
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return resultList; // LocationData 리스트 반환
    }

    public List<LocationData> selectByBusinessId(String table, long businessId) {
        db = helper.getReadableDatabase();
        List<LocationData> resultList = new ArrayList<>();

        // 쿼리 조건
        String selection = "business_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(businessId)};

        Cursor c = null;
        try {
            // 쿼리 실행
            c = db.query(table, null, selection, selectionArgs, null, null, null);

            // 커서의 각 행을 반복하면서 데이터를 수집합니다.
            while (c.moveToNext()) {
                int locationIdIndex = c.getColumnIndex("location_id");
                int turbineIdIndex = c.getColumnIndex("turbine_id");
                int latitudeIndex = c.getColumnIndex("latitude");
                int longitudeIndex = c.getColumnIndex("longitude");
                int cityIndex = c.getColumnIndex("city");
                int islandIndex = c.getColumnIndex("island");
                int createdAtIndex = c.getColumnIndex("created_at");

                // 인덱스 유효성 검사
                if (locationIdIndex == -1 || turbineIdIndex == -1 || latitudeIndex == -1 ||
                        longitudeIndex == -1 || cityIndex == -1 || islandIndex == -1 || createdAtIndex == -1) {
                    Log.e("DB Error", "One or more columns not found in the database.");
                    continue; // 컬럼이 없을 경우 다음 행으로 넘어감
                }

                // 데이터 가져오기
                long locationId = c.getLong(locationIdIndex);
                long turbineId = c.getLong(turbineIdIndex);
                double latitude = c.getDouble(latitudeIndex);
                double longitude = c.getDouble(longitudeIndex);
                String city = c.getString(cityIndex);
                String island = c.getString(islandIndex);
                String createdAt = c.getString(createdAtIndex);

                // LocationData 객체 생성 및 리스트에 추가
                resultList.add(new LocationData(locationId, businessId, turbineId, latitude, longitude, city, island, createdAt));
            }
        } catch (Exception e) {
            Log.e("DB Error", "Error executing query: " + e.getMessage(), e);
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return resultList; // LocationData 리스트 반환
    }



    public void insert(ContentValues values, String table) {
        db = helper.getWritableDatabase();
        db.insert(table, null, values);
    }

    public void update(ContentValues values, String table, String businessId) {
        db = helper.getWritableDatabase();

        db.update(table, values, "business_id=?", new String[]{businessId});
    }

    public void delete(String table, String businessId) {
        db = helper.getWritableDatabase();
        db.delete(table, "business_id=?", new String[]{businessId});
    }

    public void db_close() {
        db.close();
        helper.close();
    }
}
