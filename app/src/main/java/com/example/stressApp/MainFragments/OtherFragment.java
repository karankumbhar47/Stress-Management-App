package com.example.stressApp.MainFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.Adapter.ActivityAdapter;
import com.example.stressApp.Model.ActivityModel;
import com.example.stressApp.OthersFragments.ActivityDetails;
import com.example.stressApp.OthersFragments.MusicPlayer;
import com.example.stressApp.OthersFragments.StressMeter;
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.MainPage;
import com.example.stressApp.R;
import com.example.stressApp.Utils.JsonHelper;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class OtherFragment extends Fragment {
    private RecyclerView activity_recyclerView;
    private ActivityAdapter activityAdapter;
    private List<ActivityModel> activityModelList;
    private LoadingDialog loadingDialog;
    private FragmentManager fragmentManager;

    public OtherFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, container, false);

        init(view);
        setAdapter();

        return view;
    }

    private void init(View view){
        activity_recyclerView = view.findViewById(R.id.activity_recyclerView);
        loadingDialog = new LoadingDialog(requireActivity());
        activityModelList = new ArrayList<>();
        fragmentManager = requireActivity().getSupportFragmentManager();
    }

    private void setAdapter(){
        loadingDialog.show();
        JsonHelper.getActivityModels(requireContext(), new JsonHelper.Callback<List<ActivityModel>, String>() {
            @Override
            public void onSuccess(String customMessage, List<ActivityModel> result) {
                activityModelList = result;
                activityAdapter = new ActivityAdapter(activityModelList);
                activityAdapter.setOnItemClickListener(position -> loadFragment(position));
                activity_recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                activity_recyclerView.setAdapter(activityAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String customMessage, Exception exception, String param) {
                Utils.showToastOnMainThread(requireContext(),"Failed to Load Data");
                loadingDialog.dismiss();
            }
        });
    }

    private void load(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.commit();
    }

    @Override
    public void onResume() {
        MainPage.updateBottomNavigationBar(AppConstants.FRAGMENT_OTHERS);
        super.onResume();
    }

    private void loadFragment(int position){
        switch (position){
            case 0:
                load(new StressMeter());
                break;
            case 2:
                load(new MusicPlayer());
                break;
            default:
                load(new ActivityDetails());
        }
    }
}