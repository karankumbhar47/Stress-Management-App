package com.example.stressApp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.stressApp.AppConstants;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseUtils {

    public interface Callback<T1, T2> {
        void onSuccess(String customMessage, T1 result);
        void onFailure(String customMessage, Exception exception, T2 param);
    }

    private static final DatabaseReference databaseReference = FirebaseDatabase.getInstance(AppConstants.FIREBASE_URL).getReference().child(AppConstants.DATABASE_TEST);
    private static final DatabaseReference dataRefUsersInfo = databaseReference.child(AppConstants.DATA_USER_INFO);
    private static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public static void userLogin(Activity activity, String mobile, String password, Callback<String, String> callback) {
        Log.d(AppConstants.LOG_LOGIN, "ownerLogin: Called with \nmobile: " + mobile + "\npassword: " + password);
        dataRefUsersInfo.child(mobile).get()
                .addOnCompleteListener(taskUser -> {
                    Log.d(AppConstants.LOG_LOGIN, "getUser: Got User Id task");
                    if (taskUser.isSuccessful() && taskUser.getResult().exists()) {
                        DataSnapshot userData = taskUser.getResult();
                        DataSnapshot userCredentials = userData.child(AppConstants.DATA_CREDENTIALS);

                        if (userCredentials.child(AppConstants.KEY_PASSWORD).exists()) {
                            String old_password = Objects.requireNonNull(userCredentials.child(AppConstants.KEY_PASSWORD).getValue()).toString().trim();
                            if (password.equals(old_password.trim())) {
                                Toast.makeText(activity, "Fetching User Info...", Toast.LENGTH_SHORT).show();

                                String gmail = Objects.requireNonNull(userCredentials.child(AppConstants.KEY_GMAIL).getValue()).toString().trim();
                                String user_name = Objects.requireNonNull(userCredentials.child(AppConstants.KEY_USER_NAME).getValue()).toString().trim();

                                SharedPreferences prefCredentials = activity.getSharedPreferences(AppConstants.PREF_CREDENTIALS, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorCredentials = prefCredentials.edit();
                                editorCredentials.putString(AppConstants.KEY_MOBILE_NUMBER, mobile);
                                editorCredentials.putString(AppConstants.KEY_GMAIL, gmail);
                                editorCredentials.putString(AppConstants.KEY_PASSWORD, password);
                                editorCredentials.putString(AppConstants.KEY_USER_NAME, user_name);
                                editorCredentials.putBoolean(AppConstants.KEY_LOGIN_FLAG, true);
                                editorCredentials.apply();
                            } else {
                                dataRefUsersInfo.child(mobile).child(AppConstants.DATA_CREDENTIALS).child(AppConstants.KEY_PASSWORD).setValue(password);
                            }
                            callback.onSuccess("Login Successful!!", mobile);
                        } else {
                            callback.onFailure("Password Not Found", new Exception("Password Data Corrupted"), mobile);
                        }
                    } else {
                        callback.onFailure("Mobile Number Not Registered", new Exception("Unregistered Number"), mobile);
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Mobile Number Not Registered", null, mobile);
                    Log.d(AppConstants.LOG_LOGIN, "getUserId: User id task fail");
                });
    }

    public static void registerUser(Activity activity, String name, String mobile, String email, String password, Callback<String, String> callback) {
        dataRefUsersInfo.child(mobile).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.isComplete() && task.getResult().exists()) {
                        Log.w(AppConstants.LOG_REGISTER, "Mobile number already registered: " + mobile);
                        callback.onFailure("Mobile number already registered", new Exception("Mobile number already registered"), mobile);
                    } else {
                        createAccount(activity, email, password, mobile, name, callback);
                    }
                })
                .addOnFailureListener(e -> {
                    createAccount(activity, email, password, mobile, name, callback);
                });
    }

    private static void createAccount(Activity activity, String email, String password, String mobile, String name, Callback<String, String> callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful() && task.isComplete()) {
                        DatabaseReference dataRefCredentials = dataRefUsersInfo.child(mobile).child(AppConstants.DATA_CREDENTIALS);

                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        String userId = user.getUid();
                        Map<String, String> userData = new HashMap<>();
                        userData.put(AppConstants.KEY_USER_NAME, name);
                        userData.put(AppConstants.KEY_MOBILE_NUMBER, mobile);
                        userData.put(AppConstants.KEY_GMAIL, email);
                        userData.put(AppConstants.KEY_PASSWORD, password);
                        userData.put(AppConstants.KEY_USER_ID, userId);

                        dataRefCredentials.setValue(userData).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d(AppConstants.LOG_REGISTER, "User data saved successfully");
                                callback.onSuccess("Registration successful!", mobile);
                            } else {
                                Log.w(AppConstants.LOG_REGISTER, "Saving user data failed", task1.getException());
                                callback.onFailure("Failed to save user data", task1.getException(), mobile);
                            }
                        });
                    } else {
                        Log.w(AppConstants.LOG_REGISTER, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(activity, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        callback.onFailure("Email Registration failed", task.getException(), mobile);
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Email Registration failed", e, mobile);
                });
    }
}
