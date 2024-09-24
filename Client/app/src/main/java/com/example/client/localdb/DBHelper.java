package com.example.client.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String business =
                "CREATE TABLE IF NOT EXISTS business (" +
                        "business_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "business_title TEXT, " +
                        "created_at TIMESTAMP, " +
                        "deleted_at TIMESTAMP);";

        db.execSQL(business);

        String business_score =
                "CREATE TABLE IF NOT EXISTS business_score (" +
                        "business_score_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "business_id INTEGER, " +
                        "business_score_title TEXT NOT NULL, " +
                        "score_list_1 INTEGER, " +
                        "score_list_2 INTEGER, " +
                        "score_list_3 INTEGER, " +
                        "score_list_4 INTEGER, " +
                        "observer_name TEXT NOT NULL, " +
                        "FOREIGN KEY (business_id) REFERENCES businesses(business_id)" +
                        ");";

        db.execSQL(business_score);

        String location = "CREATE TABLE IF NOT EXISTS location (" +
                "location_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "latitude TEXT, " +
                "longitude TEXT, " +
                "city TEXT, " +
                "island TEXT, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "turbine_id INTEGER, " +
                "business_id INTEGER, " +
                "FOREIGN KEY (turbine_id) REFERENCES turbines(turbine_id), " +
                "FOREIGN KEY (business_id) REFERENCES businesses(business_id)" +
                ");";

        db.execSQL(location);

        String turbine = "CREATE TABLE IF NOT EXISTS turbine (" +
                "turbine_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "model_name TEXT NOT NULL, " +
                "rated_power INTEGER NOT NULL, " +
                "class_type TEXT NOT NULL, " +
                "cut_in_wind_speed REAL NOT NULL, " +
                "rated_wind_speed REAL NOT NULL, " +
                "cut_out_wind_speed REAL NOT NULL, " +
                "rotor_diameter REAL NOT NULL, " +
                "extreme_survival_wind_speed REAL NOT NULL, " +
                "operational_status TEXT NOT NULL" +
                ");";

        db.execSQL(turbine);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }
}
