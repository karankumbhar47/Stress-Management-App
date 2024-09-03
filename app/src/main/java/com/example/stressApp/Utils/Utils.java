package com.example.stressApp.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.stressApp.LoadingDialog;

public class Utils {
    private static AlertDialog alertDialog;

    public static void okDialog(Activity activity, String title, String message, boolean isCancelable, Runnable action) {
        activity.runOnUiThread(() -> {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Ok", (dialog, i) -> {
                if (action != null) action.run();
                dialog.dismiss();
            });
            builder.setCancelable(isCancelable);
            alertDialog = builder.create();
            alertDialog.show();
        });
    }

    public static void dismissDialog(LoadingDialog loadingDialog) {
        if (loadingDialog!= null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public static void showToastOnMainThread(final Context context, final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}