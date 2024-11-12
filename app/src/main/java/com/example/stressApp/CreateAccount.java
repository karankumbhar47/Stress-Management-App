package com.example.stressApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.Utils.FirebaseUtils;
import com.example.stressApp.Utils.LoadingDialog;

public class CreateAccount extends AppCompatActivity {
    private EditText editTextName, editTextMobile, editTextEmail, editTextPassword, editTextCnfPassword;
    private Button createAccountButton;
    private LoadingDialog loadingDialog;
    private TextView navigationLogin;
    private SharedPreferences prefCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialize UI components
        loadingDialog = new LoadingDialog(this);
        editTextName = findViewById(R.id.editTextName);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextCnfPassword = findViewById(R.id.editTextCnfPassword);
        createAccountButton = findViewById(R.id.loginButton);
        navigationLogin = findViewById(R.id.navigationLogin);
        prefCredentials = getSharedPreferences(AppConstants.PREF_CREDENTIALS, Context.MODE_PRIVATE);

        createAccountButton.setOnClickListener(v -> validateAndCreateAccount());
        navigationLogin.setOnClickListener(v -> {
            Intent intent = new Intent(CreateAccount.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void validateAndCreateAccount() {
        String name = editTextName.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String cnfPassword = editTextCnfPassword.getText().toString().trim();
        boolean isComplete = true;

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            isComplete = false;
        }

        if (TextUtils.isEmpty(mobile) || mobile.length() != 10) {
            editTextMobile.setError("Valid mobile number is required");
            editTextMobile.requestFocus();
            isComplete = false;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Valid email is required");
            editTextEmail.requestFocus();
            isComplete = false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters long");
            editTextPassword.requestFocus();
            isComplete = false;
        }

        if (!password.equals(cnfPassword)) {
            editTextCnfPassword.setError("Passwords do not match");
            editTextCnfPassword.requestFocus();
            isComplete = false;
        }

        if(isComplete)
            createAccountInFirebase(name, mobile, email, password);
    }

    private void createAccountInFirebase(String name, String mobile, String email, String password) {
        loadingDialog.show();
        FirebaseUtils.registerUser(this, name, mobile, email, password, new FirebaseUtils.Callback<String, String>() {
            @Override
            public void onSuccess(String customMessage, String result) {
                loadingDialog.dismiss();
                Toast.makeText(CreateAccount.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(String customMessage, Exception exception, String param) {
                loadingDialog.dismiss();
                Toast.makeText(CreateAccount.this, customMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        editTextName.setText("");
        editTextMobile.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        editTextCnfPassword.setText("");
    }

}