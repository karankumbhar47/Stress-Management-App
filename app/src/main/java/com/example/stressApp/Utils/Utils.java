package com.example.stressApp.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.stressApp.Model.MessageModel;
import com.example.stressApp.Model.Question;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

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

    public static void saveMessages(Context context, List<MessageModel> messageList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.PREF_CHAT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert messageList to JSON using Gson
        Gson gson = new Gson();
        String json = gson.toJson(messageList);

        // Save the JSON string in SharedPreferences
        editor.putString(AppConstants.KEY_MESSAGES, json);
        editor.apply();
    }

    public static List<MessageModel> getMessages(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.PREF_CHAT, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(AppConstants.KEY_MESSAGES, "");

        // Convert JSON back to messageList using Gson
        Gson gson = new Gson();
        Type type = new TypeToken<List<MessageModel>>() {}.getType();
        List<MessageModel> list =  gson.fromJson(json, type);
        if(list==null)
            return new ArrayList<>();
        return list;
    }

    public static String formatDateString(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("d-M-yyyy");
            Date date = inputFormat.parse(dateString);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateString;
        }
    }
}
