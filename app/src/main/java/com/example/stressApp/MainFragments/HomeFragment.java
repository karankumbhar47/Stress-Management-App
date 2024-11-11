package com.example.stressApp.MainFragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.MainPage;
import com.example.stressApp.R;

import java.util.PrimitiveIterator;

public class HomeFragment extends Fragment {
    private NavController navController;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        CardView consult_doctor_cardView = view.findViewById(R.id.consult_doctor_cardView);
        navController = NavHostFragment.findNavController(this);

        consult_doctor_cardView.setOnClickListener(v ->{
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToChatFragment());
        });
        return view;
    }

}