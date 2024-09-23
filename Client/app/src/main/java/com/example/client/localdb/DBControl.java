package com.example.client.localdb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.client.data.BusinessData;

import java.sql.Timestamp;
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


    public void insert(ContentValues values, String table) {
        db = helper.getWritableDatabase();
        db.insert(table, null, values);
    }

    public void update(ContentValues values, String table, String id) {
        db = helper.getWritableDatabase();

        db.update(table, values, "id=?", new String[]{id});
    }

    public void delete(String table, String id) {
        db = helper.getWritableDatabase();
        db.delete(table, "id=?", new String[]{id});
    }

    public void db_close() {
        db.close();
        helper.close();
    }
}
