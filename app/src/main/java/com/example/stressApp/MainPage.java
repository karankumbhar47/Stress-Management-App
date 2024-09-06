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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.stressApp.MainFragments.HomeFragment;
import com.example.stressApp.MainFragments.OtherFragment;
import com.example.stressApp.MainFragments.SettingFragment;
import com.example.stressApp.MainFragments.YogaFragment;
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainPage extends AppCompatActivity {
    private LoadingDialog loadingDialog;
    public static boolean isRootOnTop = true;
    private static BottomNavigationView bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        init();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigationBar);
        NavigationUI.setupWithNavController(bottomNav, navController);
        handleBackPressed(navController);
    }

    private void init() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        loadingDialog = new LoadingDialog(this);
        bottomNavigationBar = findViewById(R.id.bottom_navigationBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
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

    private void handleBackPressed(NavController navController) {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (navController.getCurrentDestination() != null && navController.getCurrentDestination().getId() == navController.getGraph().getStartDestination()) {
                    // Show a confirmation dialog
                    new AlertDialog.Builder(MainPage.this)
                            .setMessage("Do you really want to exit the app?")
                            .setPositiveButton("Yes", (dialog, which) -> finish()) // Exit app
                            .setNegativeButton("No", null) // Do nothing
                            .show();
                } else {
                    navController.navigateUp(); // Navigate back to the previous fragment
                }
            }
        });
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