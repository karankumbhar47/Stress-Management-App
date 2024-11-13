package com.example.stressApp.HomeFragments;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.stressApp.R;
import com.example.stressApp.Utils.MyMediaPlayer;
import com.example.stressApp.Utils.Utils;

import org.w3c.dom.Text;

public class BreathActivityFragment extends Fragment {
    private TextView textIndicator;
    private CountDownTimer timer;
    private boolean isRunning = false;
    private boolean isMusicRunning = false;
    private long minutes = 3L;
    private TextToSpeech tts;
    private SharedPreferences sharedPreferences;
    private MediaPlayer mediaPlayer;

    private static final String PREF_NAME = "BreathPrefs";
    private static final String BREATHE_COUNT = "breathe_count";
    private static final String BREATHE_MINUTES = "breathe_minutes";

    public BreathActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_breath_activity, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        textIndicator = view.findViewById(R.id.indicator);
        timer = createTimer();

        playMusic();

        ((TextView)view.findViewById(R.id.music)).setText("Pause Music");
        view.findViewById(R.id.start).setOnClickListener(v -> toggle());
        view.findViewById(R.id.music).setOnClickListener(v -> playPauseMusic(view));
        return view;
    }

    private CountDownTimer createTimer() {
        return new CountDownTimer(3 * 60000, 1000) {
            long sec = 0L;

            @Override
            public void onTick(long ms) {
                isRunning = true;
                minutes = TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms));
                sec = TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms));

                textIndicator.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, sec));

                if (minutes == 2L && sec == 57L) {
                    tts = new TextToSpeech(requireContext(), status -> {
                        if (status == TextToSpeech.SUCCESS) {
                            tts.setLanguage(Locale.US);
                            tts.setSpeechRate(0.8F);
                            tts.speak("Inhale through your nose and exhale through your mouth", TextToSpeech.QUEUE_ADD, null, null);
                        }
                    });
                }
            }

            @Override
            public void onFinish() {
                stopExercise();
            }
        };
    }

    public void stopExercise() {
        LottieAnimationView breathAnimation = requireView().findViewById(R.id.breathe);
        breathAnimation.pauseAnimation();
        isRunning = false;
        timer.cancel();
    }

    @Override
    public void onStop() {
        super.onStop();

        updateBreatheStats((int) (3L - minutes), 1);
        timer.cancel();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    private void updateBreatheStats(int minutes, int count) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BREATHE_MINUTES, sharedPreferences.getInt(BREATHE_MINUTES, 0) + minutes);
        editor.putInt(BREATHE_COUNT, sharedPreferences.getInt(BREATHE_COUNT, 0) + count);
        editor.apply();
    }


    private void toggle() {
        if (isRunning) {
            stopExercise();
            ((TextView) requireView().findViewById(R.id.start)).setText("Start");
        } else {
            LottieAnimationView breathAnimation = requireView().findViewById(R.id.breathe);
            breathAnimation.playAnimation();
            ((TextView) requireView().findViewById(R.id.start)).setText(getString(R.string.str_end));
            timer.start();
        }
    }

    private void playMusic() {
        try {
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor afd = requireContext().getAssets().openFd("music/Best Piano Beats.mp3");
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e("Error", e.getMessage(), e);
            Utils.showToastOnMainThread(requireContext(), "Error playing music");
        }
    }

    private void playPauseMusic(View view) {
        TextView musicButton = view.findViewById(R.id.music);
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            musicButton.setText("Play Music");
        }
        else{
            mediaPlayer.start();
            musicButton.setText("Pause Music");
        }
    }
}