package com.example.stressApp.OthersFragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.stressApp.MainFragments.OtherFragment;
import com.example.stressApp.MainFragments.SettingFragment;
import com.example.stressApp.Model.FAQModel;
import com.example.stressApp.R;

public class StressResult extends Fragment {
    private TextView score;
    private int scoreValue;
    private FragmentManager fragmentManager;
    private CardView backButton, closeButton;
    private LottieAnimationView free_lottie, stress_lottie;

    public StressResult(int Score) {
        this.scoreValue = Score;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stress_result, container, false);
        init(view);

        score.setText(String.format("%s/%s",scoreValue,10));
        backButton.setOnClickListener(v -> load(new OtherFragment()));
        closeButton.setOnClickListener(v -> load(new StressMeter()));
        if(scoreValue>5){
            free_lottie.setVisibility(View.VISIBLE);
            stress_lottie.setVisibility(View.GONE);
        }
        else{
            free_lottie.setVisibility(View.GONE);
            stress_lottie.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void load(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }

    private void init(View view){
        score = view.findViewById(R.id.score);
        backButton = view.findViewById(R.id.back_button);
        free_lottie = view.findViewById(R.id.free_lottie);
        stress_lottie = view.findViewById(R.id.stress_lottie);
        closeButton = view.findViewById(R.id.close_button_cardView);
        fragmentManager = requireActivity().getSupportFragmentManager();
    }
}