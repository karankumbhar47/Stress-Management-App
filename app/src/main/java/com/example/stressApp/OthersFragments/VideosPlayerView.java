package com.example.stressApp.OthersFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stressApp.Utils.LoadingDialog;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stressApp.Model.VideoModel;
import com.example.stressApp.R;

public class VideosPlayerView extends Fragment {
    private VideoModel videoModel;
    private LoadingDialog loadingDialog;
    private TextView videoType_textView;
    private TextView videoTitle_textView;
    private YouTubePlayerView youTubePlayerView;

    public VideosPlayerView() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_videos, container, false);

        videoType_textView = view.findViewById(R.id.video_title_textView);
        videoTitle_textView = view.findViewById(R.id.video_description_textView);
        loadingDialog = new LoadingDialog(requireActivity());
        loadingDialog.show();

        videoModel = VideosPlayerViewArgs.fromBundle(getArguments()).getVideos();
        videoType_textView.setText(videoModel.getTitle());
        videoTitle_textView.setText(videoModel.getDescription());
        youTubePlayerView = view.findViewById(R.id.youtubePlayerView);
        getLifecycle().addObserver(youTubePlayerView);

        configureVideoView();
        return view;
    }

    private void configureVideoView() {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoUrl = videoModel.getUrl();
                String videoId = extractVideoId(videoUrl);
                if (videoId != null) youTubePlayer.loadVideo(videoId, 0);
                if (loadingDialog.isShowing()) loadingDialog.dismiss();
            }
        });
    }

    private String extractVideoId(String url) {
        String videoId = null;
        if (url.contains("watch?v=")) {
            videoId = url.substring(url.indexOf("watch?v=") + 8);
        } else if (url.contains("youtu.be/")) {
            videoId = url.substring(url.indexOf("youtu.be/") + 9);
        }
        return videoId;
    }

    @Override
    public void onPause() {
        super.onPause();
        youTubePlayerView.release();
    }
}