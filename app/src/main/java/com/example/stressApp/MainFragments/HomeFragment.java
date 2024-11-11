package com.example.stressApp.MainFragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.R;

public class HomeFragment extends Fragment {
    private CardView diary_cardView;
    private NavController navController;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        diary_cardView = view.findViewById(R.id.diary_cardView);
        navController = NavHostFragment.findNavController(this);
        diary_cardView.setOnClickListener(v -> navController
                .navigate(HomeFragmentDirections.actionHomeFragmentToDiaryFragment()));
        return view;
    }

}