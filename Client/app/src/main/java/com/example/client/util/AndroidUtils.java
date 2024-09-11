package com.example.client.util;

import android.app.Activity;
import android.widget.Toast;

public class AndroidUtils {

    // 이전 액티비티로 돌아가는 메서드
    public static void goBack(Activity activity) {
        if (activity != null) {
            activity.onBackPressed(); // 이전 액티비티로 이동
        } else {
            Toast.makeText(activity, "Activity is null", Toast.LENGTH_SHORT).show();
        }
    }
}
