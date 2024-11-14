package com.example.stressApp.OthersFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.example.stressApp.R;

public class FidgetSpinner extends Fragment {
    private Vibrator vibrator;
    private ImageView ivFidget;
    private TextView activityInfo;
    private float speedFactor = 1.0f;

    public FidgetSpinner() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fidget_spinner, container, false);

        String description = FidgetSpinnerArgs.fromBundle(getArguments()).getDescription();
        vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
        ivFidget = view.findViewById(R.id.ivFidget);
        activityInfo = view.findViewById(R.id.activity_info);

        ivFidget.setOnTouchListener(FidgetSpinner.this::onTouch);

        activityInfo.setText(description);
        return view;
    }

    private RotateAnimation getSpinAnimation() {
        RotateAnimation spin = new RotateAnimation(
                0f,1000000f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
//        1000000f

        spin.setDuration(Math.abs(Animation.INFINITE));
//        spin.setDuration((long) (1000/speedFactor));
        spin.setRepeatCount(Animation.INFINITE);
        spin.setInterpolator(new LinearOutSlowInInterpolator());
        spin.setFillAfter(false);

        return spin;
    }

    private void startVibration() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(500000);
        }
    }

    private void stopVibration() {
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }

    private boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //speedFactor += 0.5f ;
                ivFidget.startAnimation(getSpinAnimation());
                startVibration();
                break;
            case MotionEvent.ACTION_UP:
                ivFidget.clearAnimation();
                stopVibration();
                break;
        }
        return true;
    }
}
