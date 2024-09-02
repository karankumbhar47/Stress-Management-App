package com.example.stressApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import java.util.Objects;

public class LoadingDialog extends Dialog {
    private AlertDialog dialog;

    public LoadingDialog(Activity activity) {
        super(activity);
        WindowManager.LayoutParams params = Objects.requireNonNull(getWindow()).getAttributes();
        params.gravity = Gravity.CENTER;
        getWindow().getAttributes();
        setTitle(null);
        setCancelable(false);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.progress_dialog, null);
        setContentView(view);
    }
}
