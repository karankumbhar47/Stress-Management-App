package com.example.stressApp.MainFragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stressApp.MainActivity;
import com.example.stressApp.SettingFragments.AboutusFragment;
import com.example.stressApp.SettingFragments.HelpFragment;
import com.example.stressApp.SettingFragments.SupportFragment;
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.MainPage;
import com.example.stressApp.R;

import java.nio.channels.FileChannel;

public class SettingFragment extends Fragment {
    private FragmentManager fragmentManager;
    private CardView help_cardView, share_cardView, support_cardView;
    private CardView aboutus_cardView, logout_cardView;
    private String userName, mobile_number;
    private TextView userName_textView, mobileNumber_textView;
    private SharedPreferences prefCredential;

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_setting, container, false);
        init(view);
        setListeners();
        userName_textView.setText(userName);
        mobileNumber_textView.setText(mobile_number);
        return view;
    }

    private void init(View view){
        requireActivity();
        prefCredential = requireActivity().getSharedPreferences(AppConstants.PREF_CREDENTIALS, Context.MODE_PRIVATE);
        userName = prefCredential.getString(AppConstants.KEY_USER_NAME,"user name");
        mobile_number = prefCredential.getString(AppConstants.KEY_MOBILE_NUMBER,"mobile number");

        fragmentManager = requireActivity().getSupportFragmentManager();
        help_cardView = view.findViewById(R.id.help_cardView);
        share_cardView = view.findViewById(R.id.share_app_cardView);
        support_cardView = view.findViewById(R.id.support_cardView);
        aboutus_cardView = view.findViewById(R.id.aboutUs_cardView);
        logout_cardView =view.findViewById(R.id.logout_cardView);

        mobileNumber_textView = view.findViewById(R.id.phone_number_textView);
        userName_textView = view.findViewById(R.id.user_name_textView);
    }

    private void setListeners(){
        help_cardView.setOnClickListener(v -> load(new HelpFragment()));
        support_cardView.setOnClickListener(v -> load(new SupportFragment()));
        aboutus_cardView.setOnClickListener(v -> load(new AboutusFragment()));
        logout_cardView.setOnClickListener(v -> logout());
        share_cardView.setOnClickListener(v -> shareApp());
    }

    @Override
    public void onResume() {
        MainPage.updateBottomNavigationBar(AppConstants.FRAGMENT_SETTING);
        super.onResume();
    }

    private void load(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }

    private void logout(){
        new AlertDialog.Builder(getActivity())
                .setTitle("Confirm")
                .setMessage("Are you sure you want to go back?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    for (String preference : AppConstants.PREF_LIST) {
                        SharedPreferences pref = requireActivity().getSharedPreferences(preference, MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.apply();
                    }
                    SharedPreferences.Editor editorCredentials = prefCredential.edit();
                    editorCredentials.putBoolean(AppConstants.KEY_LOGIN_FLAG, false);
                    editorCredentials.apply();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

   private void shareApp() {
       Intent sendIntent = new Intent();
       sendIntent.setAction(Intent.ACTION_SEND);

       sendIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/karankumbhar47/Stress-Management-App");
       sendIntent.setType("text/plain");

       Intent shareIntent = Intent.createChooser(sendIntent, null);
       startActivity(shareIntent);
   }
}