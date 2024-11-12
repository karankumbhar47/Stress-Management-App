package com.example.stressApp.MainFragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.appbar.MaterialToolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.MainPage;
import com.example.stressApp.R;

public class HomeFragment extends Fragment {


    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Access the toolbar and set the title and color
        MaterialToolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Home");
            toolbar.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.mid_dark_blue));
        }
        return view;
    }
}