package com.example.stressApp.SettingFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stressApp.MainFragments.SettingFragment;
import com.example.stressApp.R;
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.Utils.FirebaseUtils;
import com.example.stressApp.Utils.LoadingDialog;

public class ProfileFragment extends Fragment {
    public interface Callback{
        void onSuccess();
        void onFailure();
    }

    TextView user_name, full_name, mobile_number, email_textView;
    TextView edit_fullName, edit_mobileNumber, email_editTextView;
    ImageView profile_photo, edit_profile_button;
    LinearLayout edit_profile_view;
    Button update_profile_button;
    CardView show_profile_view;
    private NavController navController;
    boolean isDateSet;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        CardView back = view.findViewById(R.id.close_button_cardView);
        navController = NavHostFragment.findNavController(this);
        isDateSet = true;

        show_profile_view = view.findViewById(R.id.show_profile_view);
        edit_profile_button = view.findViewById(R.id.edit_profile_button);
        profile_photo = view.findViewById(R.id.profile_image_view);
        update_profile_button = view.findViewById(R.id.update_profile);
        user_name = view.findViewById(R.id.user_name);
        full_name = view.findViewById(R.id.full_name);
        mobile_number = view.findViewById(R.id.mobile_number);
        edit_profile_view = view.findViewById(R.id.edit_profile_view);
        edit_fullName = view.findViewById(R.id.edit_name);
        edit_mobileNumber = view.findViewById(R.id.edit_mobile);
        email_textView = view.findViewById(R.id.email_textView);
        email_editTextView = view.findViewById(R.id.edit_email);


        SharedPreferences prefCredentials = requireActivity().getSharedPreferences(AppConstants.PREF_CREDENTIALS, Context.MODE_PRIVATE);

        LoadingDialog loadingDialog = new LoadingDialog(this.requireActivity());
        setProfile();


        edit_profile_button.setOnClickListener(v -> {
            show_profile_view.setVisibility(show_profile_view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            edit_profile_view.setVisibility(show_profile_view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            setProfile();
        });

        update_profile_button.setOnClickListener(v -> {
            boolean isComplete = true;
            String Name_Value = edit_fullName.getText().toString().trim();
            if (!(!TextUtils.isEmpty(Name_Value) && Name_Value.length() >= 3)) {
                edit_fullName.setError("Enter Valid Name");
                isComplete = false;
            }

            loadingDialog.show();
            if (isComplete) {

                String mobile = prefCredentials.getString(AppConstants.KEY_MOBILE_NUMBER, "");
                FirebaseUtils.changeProfile(mobile,Name_Value, new Callback() {
                    @Override
                    public void onSuccess() {
                        SharedPreferences prefCredentials = requireContext().getSharedPreferences(AppConstants.PREF_CREDENTIALS,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefCredentials.edit();

                        editor.putString(AppConstants.KEY_USER_NAME,Name_Value);
                        editor.apply();

                        setProfile();
                        show_profile_view.setVisibility(show_profile_view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                        edit_profile_view.setVisibility(show_profile_view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                        loadingDialog.cancel();
                    }

                    @Override
                    public void onFailure() {
                        loadingDialog.cancel();
                    }
                });
            }
        });

        back.setOnClickListener(v -> navController.navigateUp());
        return view;
    }

    public void setProfile() {
        SharedPreferences prefCredentials = requireContext().getSharedPreferences(AppConstants.PREF_CREDENTIALS,Context.MODE_PRIVATE);

        String userName = prefCredentials.getString(AppConstants.KEY_USER_NAME,"");
        String mobileNumber = prefCredentials.getString(AppConstants.KEY_MOBILE_NUMBER,"");
        String email = prefCredentials.getString(AppConstants.KEY_GMAIL,"");

        user_name.setText(userName);
        full_name.setText(userName);
        email_textView.setText(email);
        email_editTextView.setText(email);
        edit_fullName.setText(userName);
        mobile_number.setText(mobileNumber);
        edit_mobileNumber.setText(mobileNumber);
    }
}

