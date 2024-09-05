package com.example.client.util;

import android.app.AlertDialog;
import android.content.Context;

import com.example.client.R;

public class MessageDialog {

    // INFO : 간단한 에러 다이어그램 메서드
    public void simpleErrorDialog(String title, Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setIcon(R.drawable.ic_error_24);
        dialog.setPositiveButton("확인", null);
        dialog.show();
    }

    // INFO : 에러 다이어그램 메서드
    public void errorDialog(String title, String message, Context context) {
        if (title.equals("") || title == null) {
            title = "경고!";
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIcon(R.drawable.ic_error_24);
        dialog.setPositiveButton("확인", null);
        dialog.show();
    }

    // INFO : 완료 다이어그램 메서드
    public void completeDialog(String title, String message, Context context) {
        if (title.equals("") || title == null) {
            title = "성공!";
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIcon(R.drawable.ic_check_circle_24);
        dialog.setPositiveButton("확인", null);
        dialog.show();
    }

    // INFO : 완료 다이어그램 메서드
    public void simpleCompleteDialog(String title, Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setIcon(R.drawable.ic_check_circle_24);
        dialog.setPositiveButton("확인", null);
        dialog.show();
    }
}
