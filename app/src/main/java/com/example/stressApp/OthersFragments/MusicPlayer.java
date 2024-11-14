package com.example.stressApp.OthersFragments;

import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stressApp.Adapter.MusicListAdapter;
import android.Manifest;
import android.widget.Toast;

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
    private NavController navController;
    ArrayList<AudioModel> songsList = new ArrayList<>();
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final String TAG = "Music";

    public MusicPlayer(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);
        navController = NavHostFragment.findNavController(this);
        recyclerView = view.findViewById(R.id.recycler_view);
        noMusicTextView = view.findViewById(R.id.no_songs_text);

        boolean perm = checkPermission();
        Log.d("Music", "onCreateView: permission "+perm);
        if (perm) loadMusicFiles();
         else requestPermission();
        return view;
    }

    private void loadMusicFiles() {
        Log.d("Music", "loadMusicFiles: loading music");
        String[] musicFiles = null;
        try {
            musicFiles = requireContext().getAssets().list("music");
        } catch (IOException e) {
            Log.e("Music", "loadMusicFiles: Error while loading Music",e);
            Utils.showToastOnMainThread(requireContext(),"Error while loading Music");
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
                    Log.e("Music", "loadMusicFiles: Error while fetching music",e);
                    Utils.showToastOnMainThread(requireContext(),"Error while fetching music");
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
            recyclerView.setAdapter(new MusicListAdapter(requireContext(), navController));
        }
    }

    public static String convertToMMSS(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

//    private boolean checkPermission() {
//        int result = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO);
//        return result == PackageManager.PERMISSION_GRANTED;
//    }

//    private void requestPermission() {
//        Log.d(TAG, "requestPermission: reqest ");
//        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_AUDIO)) {
//            Utils.showToastOnMainThread(requireContext(), "READ PERMISSION IS REQUIRED, PLEASE ALLOW FROM SETTINGS");
//        } else {
//            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_AUDIO}, PERMISSION_REQUEST_CODE);
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadMusicFiles();
            } else {
                noMusicTextView.setVisibility(View.VISIBLE);
                noMusicTextView.setText("Permission not granted to access music files.");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerView != null) {
            recyclerView.setAdapter(new MusicListAdapter(requireActivity().getApplicationContext(),navController));
        }
    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 and above
            int result = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        // For Android 11 and 12, return true since permission may not be required for assets
        return true;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 and above
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_AUDIO)) {
                Utils.showToastOnMainThread(requireContext(), "READ PERMISSION IS REQUIRED, PLEASE ALLOW FROM SETTINGS");
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_AUDIO}, PERMISSION_REQUEST_CODE);
            }
        } else {
            loadMusicFiles();
        }
    }
}

