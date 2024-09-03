package com.example.stressApp.MainFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.Adapter.YogaAdapter;
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.MainPage;
import com.example.stressApp.R;
import com.example.stressApp.Utils.FirebaseUtils;
import com.example.stressApp.Utils.Utils;
import com.example.stressApp.YogaDetails;
import com.example.stressApp.YogaModel;

import java.util.ArrayList;
import java.util.List;

public class YogaFragment extends Fragment {
    private RecyclerView yoga_recyclerView;
    private YogaAdapter yogaAdapter;
    private List<YogaModel> yogaModelList;
    private LoadingDialog loadingDialog;
    private FragmentManager fragmentManager;

    public YogaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_yoga, container, false);

        init(view);
        setAdapter();

        return view;
    }

    private void init(View view){
        yoga_recyclerView = view.findViewById(R.id.yoga_recyclerView);
        yogaModelList = new ArrayList<>();
        loadingDialog = new LoadingDialog(requireActivity());
        fragmentManager = requireActivity().getSupportFragmentManager();
    }

    private void setAdapter(){
        loadingDialog.show();
        FirebaseUtils.fetchYogaData(new FirebaseUtils.Callback<List<YogaModel>, String>() {
            @Override
            public void onSuccess(String customMessage, List<YogaModel> result) {
                yogaModelList = result;
                GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
                yoga_recyclerView.setLayoutManager(gridLayoutManager);
                yogaAdapter = new YogaAdapter(yogaModelList);
                yogaAdapter.setOnItemClickListener(new YogaAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        load(new YogaDetails(position+1));
                    }
                });
                yoga_recyclerView.setAdapter(yogaAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String customMessage, Exception exception, String param) {
                loadingDialog.dismiss();
                Utils.showToastOnMainThread(requireContext(),"Failed to load data");
            }
        });
    }

    private void load(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }

    @Override
    public void onResume() {
        MainPage.updateBottomNavigationBar(AppConstants.FRAGMENT_YOGA);
        super.onResume();
    }


}