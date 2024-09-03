package com.example.stressApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.stressApp.MainFragments.HomeFragment;
import com.example.stressApp.MainFragments.OtherFragment;
import com.example.stressApp.MainFragments.SettingFragment;
import com.example.stressApp.MainFragments.YogaFragment;
import com.example.stressApp.Utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {
    private Context context;
    private Activity activity;
    private String userId;
    private LoadingDialog loadingDialog;
    private static boolean isRootFragmentLoaded = true;
    private static BottomNavigationView bottomNavigationBar;
    private final FragmentManager fragmentManager = getSupportFragmentManager();

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
                R.anim.fragment_enter,
                R.anim.fragment_exit,
                R.anim.fragment_pop_enter,
                R.anim.fragment_pop_exit
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