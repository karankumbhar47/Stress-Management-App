package com.example.stressApp.OthersFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.Adapter.VideoAdapter;
import com.example.stressApp.Model.VideoModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.JsonHelper;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;

import java.util.List;

public class LearnFragment extends Fragment {
    private List<VideoModel> videosArrayList;
    private NavController navController;
    private LoadingDialog loadingDialog;

    public LearnFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn_meditation, container, false);
        navController = NavHostFragment.findNavController(this);
        loadingDialog = new LoadingDialog(requireActivity());

        loadingDialog.show();
        JsonHelper.fetchVideoInfoFromAssets(requireContext(), new JsonHelper.Callback<List<VideoModel>, String>() {
            @Override
            public void onSuccess(String customMessage, List<VideoModel> result) {
                videosArrayList = result;
                RecyclerView video_recyclerView = view.findViewById(R.id.video_recycler_view);
                video_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                VideoAdapter videoAdapter = new VideoAdapter(
                        videosArrayList, position -> navController.navigate(
                        LearnFragmentDirections.actionLearnFragmentToVideosView(videosArrayList.get(position))));
                video_recyclerView.setAdapter(videoAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String customMessage, Exception exception, String param) {
                Utils.showToastOnMainThread(requireContext(), customMessage);
                loadingDialog.dismiss();
            }
        });

        return view;
    }

}