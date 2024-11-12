package com.example.stressApp.MainFragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.appbar.MaterialToolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stressApp.R;


public class HomeFragment extends Fragment {
    private CardView diary_cardView, getting_started_cardView, consult_doctor_cardView;
    private NavController navController;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        consult_doctor_cardView = view.findViewById(R.id.consult_doctor_cardView);
        diary_cardView = view.findViewById(R.id.diary_cardView);
        getting_started_cardView = view.findViewById(R.id.getting_started_card);
        navController = NavHostFragment.findNavController(this);

        consult_doctor_cardView.setOnClickListener(v -> navController
                .navigate(HomeFragmentDirections.actionHomeFragmentToChatFragment()));

        diary_cardView.setOnClickListener(v -> navController
                .navigate(HomeFragmentDirections.actionHomeFragmentToDiaryFragment()));

        getting_started_cardView.setOnClickListener(v -> navController
                .navigate(HomeFragmentDirections.actionHomeFragmentToGettingStartedFragment()));

        // Access the toolbar and set the title and color
        MaterialToolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Home");
            toolbar.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.mid_dark_blue));
        }
        return view;
    }
}
