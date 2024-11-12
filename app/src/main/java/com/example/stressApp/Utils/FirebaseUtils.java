package com.example.stressApp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.stressApp.Model.DiaryEventModel;
import com.example.stressApp.Model.YogaModel;
import com.example.stressApp.SettingFragments.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FirebaseUtils {

    public interface Callback<T1, T2> {
        void onSuccess(String customMessage, T1 result);
        void onFailure(String customMessage, Exception exception, T2 param);
    }

    private static final DatabaseReference databaseReference = FirebaseDatabase.getInstance(AppConstants.FIREBASE_URL).getReference().child(AppConstants.DATABASE_TEST);
    private static final DatabaseReference dataRefUsersInfo  = databaseReference.child(AppConstants.DATA_USER_INFO);
    private static final DatabaseReference dataRefYogaInfo   = databaseReference.child(AppConstants.DATA_YOGA_INFO);
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

    public static void fetchYogaData(Callback<List<YogaModel>,String> callback) {
        dataRefYogaInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<YogaModel> yogaModels = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child(AppConstants.KEY_NAME).getValue(String.class);
                    String info = snapshot.child(AppConstants.KEY_INFO).getValue(String.class);
                    String stretchedPart = snapshot.child(AppConstants.KEY_STRETCHED_PART).getValue(String.class);
                    String help = snapshot.child(AppConstants.KEY_HELP).getValue(String.class);
                    String path = snapshot.child(AppConstants.KEY_PATH).getValue(String.class);

                    List<String> howToDo = new ArrayList<>();
                    DataSnapshot howToDoSnapshot = snapshot.child(AppConstants.KEY_HOW_TO_DO);
                    for (DataSnapshot stepSnapshot : howToDoSnapshot.getChildren()) {
                        howToDo.add(stepSnapshot.getValue(String.class));
                    }

                    // Tips might not be present in all records, handle accordingly
                    List<String> tips = new ArrayList<>();
                    if (snapshot.hasChild(AppConstants.KEY_TIPS)) {
                        DataSnapshot tipsSnapshot = snapshot.child("tips");
                        for (DataSnapshot tipSnapshot : tipsSnapshot.getChildren()) {
                            tips.add(tipSnapshot.getValue(String.class));
                        }
                    }

                    YogaModel yogaModel = new YogaModel(name, info, stretchedPart, help, howToDo, tips,path);
                    yogaModels.add(yogaModel);
                }

                callback.onSuccess("Data Fetched Successfully",yogaModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailure(databaseError.getMessage(),databaseError.toException(),null);
            }
        });
    }

    public static void getYogaModel(int yoga_id, Callback<YogaModel, String> callback) {
        String yogaIdString = String.valueOf(yoga_id);
        dataRefYogaInfo.child(yogaIdString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String info = dataSnapshot.child("info").getValue(String.class);
                    String stretchedPart = dataSnapshot.child("stretched-part").getValue(String.class);
                    String help = dataSnapshot.child("help").getValue(String.class);

                    List<String> howToDo = new ArrayList<>();
                    for (DataSnapshot stepSnapshot : dataSnapshot.child("how-to-do").getChildren()) {
                        howToDo.add(stepSnapshot.getValue(String.class));
                    }

                    // Retrieve "tips" list
                    List<String> tips = new ArrayList<>();
                    for (DataSnapshot tipSnapshot : dataSnapshot.child("tips").getChildren()) {
                        tips.add(tipSnapshot.getValue(String.class));
                    }

                    String path = dataSnapshot.child("path").getValue(String.class);

                    YogaModel yogaModel = new YogaModel(name, info, stretchedPart, help, howToDo, tips, path);
                    callback.onSuccess("",yogaModel);
                } else {
                    callback.onFailure("Data Not Exist",new Exception(),null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailure(databaseError.getMessage(),databaseError.toException(),null);
            }
        });
    }

    public static void changePassword(String mobileNumber, String newPassword, Callback<String, String> callback) {
        dataRefUsersInfo.child(mobileNumber).child(AppConstants.DATA_CREDENTIALS)
                .child(AppConstants.KEY_PASSWORD)
                .setValue(newPassword)
                .addOnCompleteListener(task1 -> {
                    callback.onSuccess("Successfully Updated", newPassword);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage(), e, mobileNumber));
    }

    public static void changeProfile(String mobile, String name, ProfileFragment.Callback callback) {
        dataRefUsersInfo.child(mobile).child(AppConstants.DATA_CREDENTIALS).child(AppConstants.KEY_USER_NAME)
                .setValue(name)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure();
                    }
                })
                .addOnFailureListener(e -> callback.onFailure());
    }

    public static void fetchDiaryEvent(String mobile, Callback<List<DiaryEventModel>, String> callback) {
        Log.d("Update_Diary", "updateDiaryEvent: mobile "+mobile);
        dataRefUsersInfo.child(mobile).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<DiaryEventModel> diaryEventModels = new ArrayList<>();
                        DataSnapshot messageList = task.getResult().child(AppConstants.KEY_DIARY);
                        Log.d("Update_Diary", "fetchDiaryEvent: ref "+dataRefUsersInfo.toString());
                        Log.d("Update_Diary", "fetchDiaryEvent: ref "+ messageList.getRef());
                        Log.d("Update_Diary", "fetchDiaryEvent: size "+messageList.getChildrenCount());
                        for(DataSnapshot snapshot : messageList.getChildren()) {
                            Log.d("Update_Diary", "fetchDiaryEvent: loop "+snapshot.getKey());
                            String date = snapshot.getKey();
                            String message = Objects.requireNonNull(snapshot.getValue()).toString();
                            DiaryEventModel diaryEventModel = new DiaryEventModel(message,date);
                            diaryEventModels.add(diaryEventModel);
                        }
                        callback.onSuccess("Event List Found",diaryEventModels);
                    }
                    else
                        callback.onFailure("No User Found", new Exception("User Not Found"), mobile);
                });
    }

    public static void updateDiaryEvent(String mobile,DiaryEventModel model, Callback<String,String> callback) {
        dataRefUsersInfo.child(mobile).child(AppConstants.KEY_DIARY).child(model.getDateTime())
                .setValue(model.getDescription())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                        callback.onSuccess("Event List Updated",mobile);
                    else
                        callback.onFailure("No User Found", new Exception("User Not Found"), mobile);
                });
    }

    public static void fetchSingleEvent(String mobile, String date, Callback<DiaryEventModel, String> callback) {
        dataRefUsersInfo.child(mobile).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String message = "Empty Memo";
                        Object messageObject = task.getResult()
                                .child(AppConstants.KEY_DIARY)
                                .child(date).getValue();
                        if(messageObject!=null) {
                            message = messageObject.toString();
                        }
                        callback.onSuccess("Event List Found",new DiaryEventModel(message,date));
                    }
                    else
                        callback.onFailure("No User Found", new Exception("User Not Found"), mobile);
                });
    }

}
