package com.example.stressApp.MainFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.MainPage;
import com.example.stressApp.R;

public class OtherFragment extends Fragment {
    public OtherFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_other, container, false);
    }

    @Override
    public void onResume() {
        MainPage.updateBottomNavigationBar(AppConstants.FRAGMENT_OTHERS);
        super.onResume();
    }

}