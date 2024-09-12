package com.example.stressApp;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainPage extends AppCompatActivity {
    private Toolbar toolbar;
    private LoadingDialog loadingDialog;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        init();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);



        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = Objects.requireNonNull(navHostFragment).getNavController();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigationBar);
        NavigationUI.setupWithNavController( bottomNav, navController);

        handleBackPressed(navController);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            updateToolbarForDestination(destination);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.common_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.settingFragment, false)
                .build();

        if (item.getItemId() == R.id.action_profile) {
            navController.navigate(R.id.profileFragment, null, navOptions);
            return true;
        } else if (item.getItemId() == R.id.action_help) {
            navController.navigate(R.id.helpFragment, null, navOptions);
            return true;
        } else if (item.getItemId() == R.id.action_about) {
            navController.navigate(R.id.aboutusFragment, null, navOptions);
            return true;
        }
        return false;
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        loadingDialog = new LoadingDialog(this);
    }

    private void updateToolbarForDestination(NavDestination destination) {
        int destinationId = destination.getId();
        FrameLayout submitButtonContainer = findViewById(R.id.custom_submit_button_container);

        if (destinationId == R.id.homeFragment ||
                destinationId == R.id.yogaFragment ||
                destinationId == R.id.settingFragment ||
                destinationId == R.id.otherFragment) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        } else {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        getSupportActionBar().setTitle(destination.getLabel());
        if(destinationId==R.id.settingFragment)
            toolbar.setVisibility(View.GONE);
        else{
            toolbar.setVisibility(View.VISIBLE);
            MenuItem searchItem = toolbar.getMenu().findItem(R.id.action_search);
            if (destinationId == R.id.helpFragment) {
                searchItem.setVisible(true);
                setSearch(searchItem);
            } else {
                searchItem.setVisible(false);
            }
        }

        if(destinationId==R.id.stressMeter)
            submitButtonContainer.setVisibility(View.VISIBLE);
        else
            submitButtonContainer.setVisibility(View.GONE);

    }

    private void setSearch(MenuItem searchItem){
        SearchView searchView = (SearchView) searchItem.getActionView();

        Objects.requireNonNull(searchView).setQueryHint("Search...");
        searchView.setIconifiedByDefault(true);

        // Change text appearance and search icon color
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(Color.GRAY); // Hint text color
        searchEditText.setTextColor(Color.BLACK);    // Input text color

        ImageView searchClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchClose.setColorFilter(Color.BLACK); // Change the color of the close (X) icon


        // Customize background
//        searchView.setBackgroundColor(R.drawable.search_background_color);
        searchView.setBackground(ContextCompat.getDrawable(this, R.drawable.search_background_color));


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    private void handleBackPressed(NavController navController) {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (navController.getCurrentDestination() != null && navController.getCurrentDestination().getId() == navController.getGraph().getStartDestination()) {
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

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}