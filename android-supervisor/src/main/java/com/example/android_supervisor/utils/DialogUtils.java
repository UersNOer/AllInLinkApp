package com.example.android_supervisor.utils;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.KeyEvent;

/**
 * @author wujie
 */
public class DialogUtils {

    private DialogUtils() {
        throw new AssertionError();
    }

    public static void show(Context context,
                            String message) {
        show(context, null, message, null);
    }

    public static void show(Context context,
                            String message,
                            DialogInterface.OnClickListener listener) {
        show(context, null, message, listener);
    }

    public static void show(Context context,
                            String title,
                            String message,
                            DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", listener)
                .create();

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
            }
        });
        dialog.show();
    }

    public static void askYesNo(Context context,
                                String title,
                                String message,
                                DialogInterface.OnClickListener yesListener) {
        askYesNo(context, title, message, yesListener, null);
    }

    public static void askYesNo(Context context,
                                String title,
                                String message,
                                DialogInterface.OnClickListener yesListener,
                                DialogInterface.OnClickListener noListener) {
        show(context, title, message, "是", "否", yesListener, noListener);
    }

    public static void askOKCancel(Context context,
                                   String title,
                                   String message,
                                   DialogInterface.OnClickListener okListener) {
        askOKCancel(context, title, message, okListener, null);
    }

    public static void askOKCancel(Context context,
                                   String title,
                                   String message,
                                   DialogInterface.OnClickListener okListener,
                                   DialogInterface.OnClickListener cancelListener) {
        show(context, title, message, "确定", "取消", okListener, cancelListener);
    }

    public static void show(Context context,
                                   String title,
                                   String message,
                                   String positiveButtonText,
                                   String negativeButtonText,
                                   DialogInterface.OnClickListener positiveButtonListener,
                                   DialogInterface.OnClickListener negativeButtonListener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, positiveButtonListener)
                .setNegativeButton(negativeButtonText, negativeButtonListener)
                .create();

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
            }
        });
        dialog.show();
    }
}
