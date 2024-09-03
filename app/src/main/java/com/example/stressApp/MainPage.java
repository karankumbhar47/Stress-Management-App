package com.example.stressApp;

import android.app.AlertDialog;
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
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {
    private LoadingDialog loadingDialog;
    public static boolean isRootOnTop = true;
    private static BottomNavigationView bottomNavigationBar;
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        init();

        if (savedInstanceState == null)
            load(new HomeFragment());
    }

    private void init() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        loadingDialog = new LoadingDialog(this);
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPress();
            }
        });


        bottomNavigationBar = findViewById(R.id.bottom_navigationBar);
        bottomNavigationBar.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.setting_icon) {
                load(new SettingFragment());
            } else if (id == R.id.others_icon) {
                load(new OtherFragment());
            } else if (id == R.id.yoga_icon) {
                load(new YogaFragment());
            } else {
                load(new HomeFragment());
            }
            return true;
        });
    }

    private void handleBackPress(){
        new AlertDialog.Builder(this)
                .setTitle("Exit Application")
                .setMessage("Are you sure you want to Exit the App")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }

    private void load(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }

    public static void updateBottomNavigationBar(String fragment) {
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