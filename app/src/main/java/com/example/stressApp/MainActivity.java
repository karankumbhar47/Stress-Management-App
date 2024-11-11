package com.example.stressApp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.Utils.FirebaseUtils;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText mobile_text, password_text;
    private LoadingDialog loadingDialog;
    private boolean isLogin;
    private Activity activity;
    private Typeface customFont;
    private SharedPreferences prefCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();
        setListeners();

        if (isLogin) {
            Intent intent = new Intent(MainActivity.this, MainPage.class);
            startActivity(intent);
            finish();
        }
    }

    private void init() {
        activity = this;
        prefCredentials = getSharedPreferences(AppConstants.PREF_CREDENTIALS, MODE_PRIVATE);
        customFont = ResourcesCompat.getFont(this, R.font.font1);
        isLogin = prefCredentials.getBoolean(AppConstants.KEY_LOGIN_FLAG, false);
        loadingDialog = new LoadingDialog(MainActivity.this);
        mobile_text = findViewById(R.id.editTextMobile);
        password_text = findViewById(R.id.editTextPassword);
    }

    private void setListeners() {
        TextView create_account_button = findViewById(R.id.signupNavigation);
        TextView forgetPassword_text = findViewById(R.id.forgot_password);
        Button login_button = findViewById(R.id.loginButton);
        CheckBox password_check_box = findViewById(R.id.showPassword_checkBox);

        create_account_button.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, CreateAccount.class);
            startActivity(intent);
        });
        login_button.setOnClickListener(v -> {
            String number = mobile_text.getText().toString();
            String password = password_text.getText().toString();
            if (TextUtils.isEmpty(number) || TextUtils.isEmpty(password)) {
                Toast.makeText(MainActivity.this, "Empty Credentials !", Toast.LENGTH_SHORT).show();
            } else {
                userLogin(number, password);
            }
        });


        forgetPassword_text.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Forget Password")
                    .setMessage("Are you sure you want to change your password?")
                    .setPositiveButton("Proceed", (dialog, which) -> {
                        View dialogView = getLayoutInflater().inflate(R.layout.forget_password_layout, null);

                        EditText emailEditText = dialogView.findViewById(R.id.emailEditText);
                        Button confirmButton = dialogView.findViewById(R.id.confirmButton);

                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Forget Password")
                                .setView(dialogView)
                                .create();

                        confirmButton.setOnClickListener(confirmView -> {
                            String emailAddress = emailEditText.getText().toString().trim();
                            if (TextUtils.isEmpty(emailAddress) || !emailAddress.contains("@")) {
                                emailEditText.setError("Enter your valid email");
                            } else {
                                loadingDialog.show();
                                forgetPassword(emailAddress);
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
        password_check_box.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                password_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                password_text.setTypeface(customFont);
            } else {
                password_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password_text.setTypeface(customFont);
            }
        });
    }

    private void userLogin(String mobile, String password) {
        loadingDialog.show();
        FirebaseUtils.userLogin(this, mobile, password, new FirebaseUtils.Callback<String, String>() {
            @Override
            public void onSuccess(String customMessage, String result) {
                Utils.okDialog(activity, "Login Successful", "User Info Saved Successfully", false, () -> {
                    SharedPreferences.Editor editor = prefCredentials.edit();
                    editor.putString(AppConstants.KEY_MOBILE_NUMBER, mobile);
                    editor.putString(AppConstants.KEY_PASSWORD, password);
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, MainPage.class);
                    startActivity(intent);
                    if (loadingDialog != null && loadingDialog.isShowing() && !isFinishing()) {
                        loadingDialog.dismiss();
                    }
                    finish();
                });
            }

            @Override
            public void onFailure(String customMessage, Exception exception, String param) {
                if (loadingDialog != null && loadingDialog.isShowing() && !isFinishing()) {
                    loadingDialog.dismiss();
                }
                Utils.okDialog(activity, "Login Failed", customMessage, true, null);
            }
        });
    }

    public void forgetPassword(String emailAddress) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(this)
                                .setTitle("Email Sent")
                                .setMessage("An email has been sent to your email address with instructions on how to reset your password. Please check your inbox and follow the link provided to change your password.")
                                .setPositiveButton("OK", null)
                                .show();
                        loadingDialog.dismiss();
                    } else {
                        Toast.makeText(this, "Failed to send password reset email. Please try again later.", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });
    }

}