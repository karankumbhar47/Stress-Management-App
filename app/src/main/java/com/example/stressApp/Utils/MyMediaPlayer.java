package com.example.stressApp.Utils;

import android.media.MediaPlayer;

import com.example.stressApp.Model.AudioModel;

import java.util.ArrayList;

public class MyMediaPlayer {
    static MediaPlayer instance;

    public static MediaPlayer getInstance(){
        if(instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }

    public static void release() {
        if (instance != null) {
            instance.release();
            instance = null;
        }
    }

    public static int currentIndex = -1;
    public static ArrayList<AudioModel> songList;

}
