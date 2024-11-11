package com.example.stressApp.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.stressApp.Model.Question;
import com.example.stressApp.Model.VideoModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }

    public static void showLongToast(final Context context, final String message) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, message, Toast.LENGTH_LONG).show());
    }


    public static List<Question> getRandomQuestions(List<Question> allQuestions, int numberOfQuestions) {
        Collections.shuffle(allQuestions);
        return allQuestions.subList(0, Math.min(numberOfQuestions, allQuestions.size()));
    }

    public static String formatDateString(String dateString) {
        try {
            // Parse the input date string
            SimpleDateFormat inputFormat = new SimpleDateFormat("d-M-yyyy");
            Date date = inputFormat.parse(dateString);

            // Format the date to dd-MM-yyyy
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateString; // Return original if parsing fails
        }
    }


}
