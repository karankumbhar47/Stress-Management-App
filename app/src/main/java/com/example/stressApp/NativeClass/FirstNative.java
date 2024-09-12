package com.example.stressApp.NativeClass;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.R;
import com.example.stressApp.Utils.AppConstants;

public class FirstNative extends Fragment {


    public FirstNative() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_first_native, container, false);
        Log.d(AppConstants.LOG_NATIVE, "On Create Method Calling Native Library");
        NativeLibrary.init();
        return view;
    }
}