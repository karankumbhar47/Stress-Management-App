package com.example.stressApp.MainFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.Adapter.YogaAdapter;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.R;
import com.example.stressApp.Utils.FirebaseUtils;
import com.example.stressApp.Utils.Utils;
import com.example.stressApp.Model.YogaModel;

import java.util.ArrayList;
import java.util.List;

public class YogaFragment extends Fragment {
    private RecyclerView yoga_recyclerView;
    private YogaAdapter yogaAdapter;
    private List<YogaModel> yogaModelList;
    private LoadingDialog loadingDialog;
    private NavController navController;

    public YogaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yoga, container, false);

        if (isAdded()) {
            init(view);
            setAdapter();
        }

        return view;
    }

    private void init(View view) {
        yoga_recyclerView = view.findViewById(R.id.yoga_recyclerView);
        yogaModelList = new ArrayList<>();
        loadingDialog = new LoadingDialog(requireActivity());
        navController = NavHostFragment.findNavController(this);
    }

    private void setAdapter() {
        loadingDialog.show();

        if (!Utils.isNetworkAvailable(requireContext())) {
            loadingDialog.dismiss();
            Utils.showToastOnMainThread(requireContext(), "No internet connection");
            return;
        }
        FirebaseUtils.fetchYogaData(new FirebaseUtils.Callback<List<YogaModel>, String>() {
            @Override
            public void onSuccess(String customMessage, List<YogaModel> result) {
                yogaModelList = result;
                GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
                yoga_recyclerView.setLayoutManager(gridLayoutManager);
                yogaAdapter = new YogaAdapter(yogaModelList);
                yogaAdapter.setOnItemClickListener(position -> {
                    YogaFragmentDirections.ActionYogaFragmentToYogaDetails action = YogaFragmentDirections.actionYogaFragmentToYogaDetails(position);
                    navController.navigate(action);
                });

                yoga_recyclerView.setAdapter(yogaAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String customMessage, Exception exception, String param) {
                loadingDialog.dismiss();
                Utils.showToastOnMainThread(requireContext(), "Failed to load data");
            }
        });
    }
}