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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stressApp.MainActivity;
import com.example.stressApp.SettingFragments.AboutusFragment;
import com.example.stressApp.SettingFragments.HelpFragment;
import com.example.stressApp.SettingFragments.LanguageFragment;
import com.example.stressApp.SettingFragments.ProfileFragment;
import com.example.stressApp.SettingFragments.SupportFragment;
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.MainPage;
import com.example.stressApp.R;
import com.example.stressApp.Utils.FirebaseUtils;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

public class SettingFragment extends Fragment {
    private CardView help_cardView, share_cardView, support_cardView,language_cardView;
    private CardView aboutus_cardView, logout_cardView, changePassword_cardView;
    private String userName, mobile_number;
    private TextView userName_textView, mobileNumber_textView;
    private ImageView editProfile_button;
    private SharedPreferences prefCredential;
    private LoadingDialog loadingDialog;
    private NavController navController;

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
        loadingDialog = new LoadingDialog(requireActivity());
        prefCredential = requireActivity().getSharedPreferences(AppConstants.PREF_CREDENTIALS, Context.MODE_PRIVATE);
        userName = prefCredential.getString(AppConstants.KEY_USER_NAME,"user name");
        mobile_number = prefCredential.getString(AppConstants.KEY_MOBILE_NUMBER,"mobile number");

        navController = NavHostFragment.findNavController(this);
        help_cardView = view.findViewById(R.id.help_cardView);
        share_cardView = view.findViewById(R.id.share_app_cardView);
        support_cardView = view.findViewById(R.id.support_cardView);
        aboutus_cardView = view.findViewById(R.id.aboutUs_cardView);
        logout_cardView =view.findViewById(R.id.logout_cardView);
        changePassword_cardView = view.findViewById(R.id.change_password_cardView);
        language_cardView = view.findViewById(R.id.language_cardView);
        editProfile_button = view.findViewById(R.id.profile_imageView);

        mobileNumber_textView = view.findViewById(R.id.phone_number_textView);
        userName_textView = view.findViewById(R.id.user_name_textView);
    }

    private void setListeners(){
        help_cardView.setOnClickListener(v -> navController.navigate(R.id.action_settingFragment_to_helpFragment));
        support_cardView.setOnClickListener(v -> navController.navigate(R.id.action_settingFragment_to_supportFragment));
        aboutus_cardView.setOnClickListener(v -> navController.navigate(R.id.action_settingFragment_to_aboutusFragment2));
        logout_cardView.setOnClickListener(v -> logout());
        share_cardView.setOnClickListener(v -> shareApp());
        changePassword_cardView.setOnClickListener(v -> showBottomSheetDialog());
        language_cardView.setOnClickListener(v -> navController.navigate(R.id.action_settingFragment_to_languageFragment));
        editProfile_button.setOnClickListener(v -> navController.navigate(R.id.action_settingFragment_to_profileFragment2));
    }

    private void showBottomSheetDialog(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
        bottomSheetDialog.setContentView(R.layout.change_pass);
        Button changeButton = bottomSheetDialog.findViewById(R.id.changepass);
        EditText newPassword_editText = bottomSheetDialog.findViewById(R.id.password);

        Objects.requireNonNull(changeButton).setOnClickListener(v -> {
            String newPassword = Objects.requireNonNull(newPassword_editText).getText().toString().trim();
            Log.d(AppConstants.LOG_SETTING, "showBottomSheetDialog: new password "+newPassword);
            Log.d(AppConstants.LOG_SETTING, "showBottomSheetDialog: new password size "+newPassword.length());
            Log.d(AppConstants.LOG_SETTING, "showBottomSheetDialog: mobile number "+mobile_number);
            if(newPassword.length()<= 5){
                Utils.showToastOnMainThread(requireContext(), "Please type a stronger password !!!");
            }
            else{
                String mobileNumber = prefCredential.getString(AppConstants.KEY_MOBILE_NUMBER,"");
                loadingDialog.show();
                FirebaseUtils.changePassword(mobileNumber, newPassword, new FirebaseUtils.Callback<String, String>() {
                    @Override
                    public void onSuccess(String customMessage, String result) {
                        loadingDialog.dismiss();
                        bottomSheetDialog.dismiss();
                        Utils.showLongToast(requireContext(),"\""+newPassword+"\"\nNew Password Set Successfully");
                    }

                    @Override
                    public void onFailure(String customMessage, Exception exception, String params) {
                        loadingDialog.dismiss();
                        Utils.showLongToast(requireContext(),customMessage);
                    }
                });
            }
        });
        bottomSheetDialog.show();
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