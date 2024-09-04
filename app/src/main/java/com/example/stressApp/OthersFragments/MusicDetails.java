package com.example.stressApp.OthersFragments;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.stressApp.Model.AudioModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.MyMediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicDetails extends Fragment {
    TextView titleTv,currentTimeTv,totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlay,nextBtn,previousBtn,musicIcon;
    ArrayList<AudioModel> songsList;
    AudioModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x=0;

    public MusicDetails(ArrayList<AudioModel> songsList) {
        this.songsList = songsList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_details, container, false);

        titleTv = view.findViewById(R.id.song_title);
        currentTimeTv = view.findViewById(R.id.current_time);
        totalTimeTv = view.findViewById(R.id.total_time);
        seekBar = view.findViewById(R.id.seek_bar);
        pausePlay = view.findViewById(R.id.pause_play);
        nextBtn = view.findViewById(R.id.next);
        previousBtn = view.findViewById(R.id.previous);
        musicIcon = view.findViewById(R.id.music_icon_big);
        titleTv.setSelected(true);


        setResourcesWithMusic();

        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));

                    if(mediaPlayer.isPlaying()){
                        pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                        musicIcon.setRotation(x++);
                    }else{
                        pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        musicIcon.setRotation(0);
                    }

                }
                new Handler().postDelayed(this,100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }


    void setResourcesWithMusic(){
        currentSong = songsList.get(MyMediaPlayer.currentIndex);

        titleTv.setText(currentSong.getTitle().split("\\.")[0]);

        totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));

        pausePlay.setOnClickListener(v-> pausePlay());
        nextBtn.setOnClickListener(v-> playNextSong());
        previousBtn.setOnClickListener(v-> playPreviousSong());

        playMusic();


    }

//    private void playMusic(){
//
//        mediaPlayer.reset();
//        try {
//            mediaPlayer.setDataSource(currentSong.getPath());
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//            seekBar.setProgress(0);
//            seekBar.setMax(mediaPlayer.getDuration());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }

    private void playMusic() {
        mediaPlayer.reset();
        try {
            AssetFileDescriptor afd = requireContext().getAssets().openFd("music/" + currentSong.getFileName());
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void playNextSong(){

        if(MyMediaPlayer.currentIndex== songsList.size()-1)
            return;
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    private void playPreviousSong(){
        if(MyMediaPlayer.currentIndex== 0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void pausePlay(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

}