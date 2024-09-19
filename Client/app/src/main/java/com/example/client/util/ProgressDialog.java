package com.example.client.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.client.R;

public class ProgressDialog extends Dialog {
    public ProgressDialog(@NonNull Context context) {
        super(context);
        // 다이얼 로그 제목을 안보이도록
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
    }
}
