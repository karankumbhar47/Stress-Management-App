package com.example.stressApp.HomeFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.R;

public class GettingStartedFragment extends Fragment {

    public GettingStartedFragment() {
    }

    private TextView welcomeText, stressMeterText, exerciseText, diaryText, quoteText;
    private Button stressMeterButton, exerciseButton, diaryButton, musicButton, breathingButton;
    private NavController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_getting_started, container, false);

        // Initialize views
        controller = NavHostFragment.findNavController(this);
        welcomeText = view.findViewById(R.id.welcomeText);
        stressMeterText = view.findViewById(R.id.stressMeterText);
        exerciseText = view.findViewById(R.id.exerciseText);
        diaryText = view.findViewById(R.id.diaryText);
        quoteText = view.findViewById(R.id.quoteText);

        stressMeterButton = view.findViewById(R.id.stressMeterButton);
        exerciseButton = view.findViewById(R.id.exerciseButton);
        diaryButton = view.findViewById(R.id.diaryButton);
        musicButton = view.findViewById(R.id.musicButton);
        breathingButton = view.findViewById(R.id.breathingButton);

        // Set welcome text and quote
        welcomeText.setText("Welcome To Our Stress APP");
        quoteText.setText("“Taking time to recharge your mind can boost productivity and happiness.”");

        // Set button click listeners with example actions
        stressMeterButton.setOnClickListener(v -> {
                    Toast.makeText(requireContext(), "Stress Meter is loading...", Toast.LENGTH_SHORT).show();
                    controller.navigate(R.id.action_gettingStartedFragment_to_stressMeter);
                });

        exerciseButton.setOnClickListener(v -> {
                    Toast.makeText(requireContext(), "Exercise Guidelines are loading...", Toast.LENGTH_SHORT).show();
                    controller.navigate(R.id.action_gettingStartedFragment_to_yogaFragment);
                });

        diaryButton.setOnClickListener(v -> {
                    Toast.makeText(requireContext(), "Diary Reading section is opening...", Toast.LENGTH_SHORT).show();
                    controller.navigate(R.id.action_gettingStartedFragment_to_diaryFragment);
                });

        // Add click listeners for the new sections
        musicButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Playing Relaxing Music...", Toast.LENGTH_SHORT).show();
            controller.navigate(R.id.action_gettingStartedFragment_to_musicPlayer);
        });

        breathingButton.setOnClickListener(v ->{
                Toast.makeText(requireContext(), "Starting Breathing Exercises...", Toast.LENGTH_SHORT).show();
                controller.navigate(R.id.action_gettingStartedFragment_to_musicPlayer);
        });
        return view;
    }

}

