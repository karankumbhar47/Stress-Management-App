package com.example.stressApp.OthersFragments;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stressApp.Adapter.MusicListAdapter;
import android.Manifest;
import com.example.stressApp.Model.AudioModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MusicPlayer extends Fragment {
    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<AudioModel> songsList = new ArrayList<>();
    FragmentManager fragmentManager;

    public MusicPlayer() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);
        fragmentManager = requireActivity().getSupportFragmentManager();

        recyclerView = view.findViewById(R.id.recycler_view);
        noMusicTextView = view.findViewById(R.id.no_songs_text);

        if (checkPermission() == false) {
            requestPermission();
            return view;
        }

        // Fetch music files from assets
        String[] musicFiles = null;
        try {
            musicFiles = requireContext().getAssets().list("music");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (musicFiles != null) {
            for (String fileName : musicFiles) {
                // Assuming you have a method to get the duration of the file
                String duration = "0"; // Placeholder, you'll need to calculate or fetch duration
                AudioModel songData = new AudioModel(fileName, fileName, duration);
                songsList.add(songData);
            }
        }
//
//        String[] projection = {
//                MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.DURATION
//        };
//
//        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
//        Cursor cursor = requireContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
//        while (Objects.requireNonNull(cursor).moveToNext()) {
//            AudioModel songData = new AudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2));
//            if (new File(songData.getPath()).exists())
//                songsList.add(songData);
//        }

        if (songsList.isEmpty()) {
            noMusicTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(new MusicListAdapter(songsList, requireActivity().getApplicationContext(),fragmentManager));
        }
        return view;
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
            recyclerView.setAdapter(new MusicListAdapter(songsList,requireActivity().getApplicationContext(),fragmentManager));
        }
    }
}

