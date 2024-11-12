package com.example.stressApp.Utils;

import android.content.Context;
import android.util.Log;

import com.example.stressApp.Model.ActivityModel;
import com.example.stressApp.Model.FAQModel;
import com.example.stressApp.Model.Question;
import com.example.stressApp.Model.StudentModel;
import com.example.stressApp.Model.VideoModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonHelper {
    public interface Callback<T1, T2> {
        void onSuccess(String customMessage, T1 result);
        void onFailure(String customMessage, Exception exception, T2 param);
    }

    public static void getActivityModels(Context context, Callback<List<ActivityModel>,String> callback) {
        List<ActivityModel> activityModelList = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("jsonData/activities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray activitiesArray = jsonObject.getJSONArray("activities");
            for (int i = 0; i < activitiesArray.length(); i++) {
                JSONObject activityObject = activitiesArray.getJSONObject(i);
                int id = activityObject.getInt("id");
                String name = activityObject.getString("name");
                String activityIconPath = activityObject.getString("icon_key");
                String activityInfo = activityObject.getString("activity_info");

                ActivityModel activityModel = new ActivityModel(id, name, activityIconPath, activityInfo);
                activityModelList.add(activityModel);
            }
            callback.onSuccess("Successfully Loaded",activityModelList);
        } catch (IOException | JSONException e) {
            Log.e("JsonHelper", "Error reading JSON", e);
            callback.onFailure(e.getMessage(),e,"activity");
        }
    }

    public static void getStudentList(Context context,Callback<List<StudentModel>,String> callback) {
        List<StudentModel> list = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("jsonData/StudentInfo.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonFileString = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(jsonFileString);
            JSONArray studentArray = jsonObject.getJSONArray("StudentInfo");
            for (int i = 0; i < studentArray.length(); i++) {
                JSONObject studentObject = studentArray.getJSONObject(i);
                String name = studentObject.getString("name");
                String id_number = studentObject.getString("id_number");
                String email = studentObject.getString("email");
                String phone_number = studentObject.getString("phone_number");
                String role = studentObject.getString("role");

                StudentModel studentModel = new StudentModel(name, id_number,email,phone_number,role);
                list.add(studentModel);
            }
            callback.onSuccess("Successfully Loaded",list);
        } catch (IOException | JSONException e) {
            Log.e("JsonUtils", "Error reading JSON file", e);
            callback.onFailure(e.getMessage(),e,"student");
        }
    }

    public static void fetchQuestionsFromAssets(Context context, Callback<List<Question>, String> callback) {
        List<Question> questions = new ArrayList<>();

        try {
            InputStream is = context.getAssets().open("jsonData/questions.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Parse the JSON
            JSONObject jsonObject = new JSONObject(json);
            JSONArray questionsArray = jsonObject.getJSONArray("questions");

            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObject = questionsArray.getJSONObject(i);
                JSONArray optionsArray = questionObject.getJSONArray("options");

                String question = questionObject.getString("question");
                List<Question.Option> options = new ArrayList<>();

                for (int j = 0; j < optionsArray.length(); j++) {
                    JSONObject optionObject = optionsArray.getJSONObject(j);
                    String optionText = optionObject.getString("text");
                    int optionPoints = optionObject.getInt("points");
                    Question.Option option = new Question.Option(optionText, optionPoints);
                    options.add(option);
                }

                Question questionModel = new Question(question, options);
                questions.add(questionModel);
            }

            callback.onSuccess("Successfully Loaded", questions);
        } catch (IOException | org.json.JSONException e) {
            callback.onFailure(e.getMessage(), e, "question");
        }
    }

    public static void fetchVideoInfoFromAssets(Context context, Callback<List<VideoModel>, String> callback) {
        List<VideoModel> list = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("jsonData/meditation.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonFileString = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(jsonFileString);
            JSONArray meditationInfoList = jsonObject.getJSONArray("Meditation-Info");
            for (int i = 0; i < meditationInfoList.length(); i++) {
                JSONObject meditationObject = meditationInfoList.getJSONObject(i);
                String url = meditationObject.getString("url");
                String title = meditationObject.getString("title");
                String description = meditationObject.getString("description");
                String duration = meditationObject.getString("duration");
                int thumbnail = meditationObject.getInt("thumbnail");

                VideoModel videoModel = new VideoModel(url, title, description, duration, thumbnail);
                list.add(videoModel);
            }
            callback.onSuccess("Successfully Loaded", list);
        } catch (IOException | JSONException e) {
            Log.e("JsonUtils", "Error reading JSON file", e);
            callback.onFailure(e.getMessage(), e, "student");
        }
    }
}

