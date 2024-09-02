package com.example.stressApp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.stressApp.MainFragments.HomeFragment;
import com.example.stressApp.MainFragments.OtherFragment;
import com.example.stressApp.MainFragments.SettingFragment;
import com.example.stressApp.MainFragments.YogaFragment;
import com.example.stressApp.Utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainPage extends AppCompatActivity {
    // static fields
    private static String userId;
    public static BottomNavigationView bottomNavigationBar;
    private static boolean isRootFragmentLoaded = true;

    private Context context;
    private Activity activity;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        init();

        if (savedInstanceState == null)
            load(new HomeFragment(), fragmentManager, true);

    }

    private void init() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPress();
            }
        });

        context = this;
        activity = this;
        loadingDialog = new LoadingDialog(this);
        bottomNavigationBar = findViewById(R.id.bottom_navigationBar);
        SharedPreferences prefCredential = getSharedPreferences(AppConstants.PREF_CREDENTIALS, MODE_PRIVATE);
        userId = prefCredential.getString(AppConstants.KEY_USER_ID, "");

        bottomNavigationBar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.setting_icon) {
                load(new SettingFragment(), fragmentManager, true);
            } else if (id == R.id.others_icon) {
                load(new OtherFragment(), fragmentManager, true);
            } else if (id == R.id.yoga_icon) {
                load(new YogaFragment(), fragmentManager, true);
            } else {
                load(new HomeFragment(), fragmentManager, true);
            }
            return true;
        });

    }

    private void handleBackPress() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (isRootFragmentLoaded || backStackEntryCount == 0) {
            if (!isFinishing() && !isDestroyed()) {
                new AlertDialog.Builder(context)
                        .setTitle("Exit Application")
                        .setMessage("Are you sure you want to Exit the App")
                        .setPositiveButton("Yes", (dialog, which) -> finish())
                        .setNegativeButton("No", null)
                        .show();
            }
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public static void load(Fragment fragment, FragmentManager fragmentManager, boolean isRootFragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.fragment_enter,  // enter
                R.anim.fragment_exit,   // exit
                R.anim.fragment_pop_enter,  // popEnter
                R.anim.fragment_pop_exit   // popExit
        );
        if (isRootFragment) {
            isRootFragmentLoaded = true;
            fragmentTransaction.add(R.id.frame, fragment);
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack(AppConstants.ROOT_FRAGMENT_TAG, 0);
            }
        } else {
            isRootFragmentLoaded = false;
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();

    }

    public static void goBack(Fragment fragment) {
        FragmentManager fm = fragment.requireActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            fragment.requireActivity().onBackPressed();
        }
    }

    public static void updateBottomNavigationBar(String fragment, Context context) {
        Integer menuItemId = AppConstants.fragmentMap.get(fragment);
        if (menuItemId != null) {
            MenuItem item = bottomNavigationBar.getMenu().findItem(menuItemId);
            if (item != null) {
                item.setChecked(true);
            }
        }
    }

    @Override
    public void onDestroy() {
        Utils.dismissDialog(loadingDialog);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Utils.dismissDialog(loadingDialog);
        super.onPause();
    }

}