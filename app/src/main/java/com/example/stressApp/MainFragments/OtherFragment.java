package com.example.stressApp.MainFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.Adapter.ActivityAdapter;
import com.example.stressApp.Model.ActivityModel;
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
    private NavController navController;

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
        navController = NavHostFragment.findNavController(this);
    }

    private void setAdapter(){
        loadingDialog.show();
        JsonHelper.getActivityModels(requireContext(), new JsonHelper.Callback<List<ActivityModel >, String>() {
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

    private void loadFragment(int position){
        switch (position){
            case 0:
                navController.navigate(R.id.action_otherFragment_to_stressMeter);
                break;
            case 1:
                navController.navigate(R.id.action_otherFragment_to_fidgetSpinner);
                break;
            case 2:
                navController.navigate(R.id.action_otherFragment_to_musicPlayer);
                break;
            case 3:
                navController.navigate(R.id.action_otherFragment_to_fragment_native);
                break;
            default:
                navController.navigate(R.id.action_otherFragment_to_fidgetCube);
        }
    }
}