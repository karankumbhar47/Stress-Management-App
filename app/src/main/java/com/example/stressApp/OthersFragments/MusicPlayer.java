package com.example.stressApp.OthersFragments;

import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stressApp.Adapter.MusicListAdapter;
import android.Manifest;
import com.example.stressApp.Model.AudioModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.MyMediaPlayer;
import com.example.stressApp.Utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends Fragment {
    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<AudioModel> songsList = new ArrayList<>();
    private NavController navController;
    private CardView close_button;

    public MusicPlayer() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);

        navController = NavHostFragment.findNavController(this);
        recyclerView = view.findViewById(R.id.recycler_view);
        noMusicTextView = view.findViewById(R.id.no_songs_text);
        close_button = view.findViewById(R.id.close_button_cardView);
        close_button.setOnClickListener(v -> navController.navigateUp());

        if (!checkPermission()) {
            requestPermission();
            return view;
        }

        String[] musicFiles = null;
        try {
            musicFiles = requireContext().getAssets().list("music");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (musicFiles != null) {
            for (String fileName : musicFiles) {
                MediaPlayer mp = new MediaPlayer();
                try {
                    AssetFileDescriptor afd = requireContext().getAssets().openFd("music/" + fileName);
                    mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mp.prepare();
                    String duration = convertToMMSS(mp.getDuration());
                    AudioModel songData = new AudioModel(fileName, fileName, duration);
                    songsList.add(songData);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mp.release();
                }
            }
        }


        if (songsList.isEmpty()) {
            noMusicTextView.setVisibility(View.VISIBLE);
        } else {
            MyMediaPlayer.songList = songsList;
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(new MusicListAdapter( requireContext(), navController));
        }
        return view;
    }

    public static String convertToMMSS(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Utils.showToastOnMainThread(requireContext(), "READ PERMISSION IS REQUIRED, PLEASE ALLOW FROM SETTINGS");
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerView != null) {
            recyclerView.setAdapter(new MusicListAdapter(requireActivity().getApplicationContext(),navController));
        }
    }
}

