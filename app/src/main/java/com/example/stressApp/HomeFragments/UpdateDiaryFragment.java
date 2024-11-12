package com.example.stressApp.HomeFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stressApp.Model.DiaryEventModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.Utils.FirebaseUtils;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateDiaryFragment extends Fragment {
    private String mobile;
    private CardView clearButton;
    private CardView saveButton;
    private EditText description_textView;
    private LoadingDialog loadingDialog;
    private String dateString;

    public UpdateDiaryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diary_update_activity, container, false);

        init(view);

        fillDetails();

        clearButton.setOnClickListener(v -> description_textView.setText(""));

        saveButton.setOnClickListener(v -> {
            String description = description_textView.getText().toString();
            if (!description.isEmpty()) {
                loadingDialog.show();
                FirebaseUtils.updateDiaryEvent(mobile, new DiaryEventModel(description, dateString), new FirebaseUtils.Callback<String, String>() {
                    @Override
                    public void onSuccess(String customMessage, String result) {
                        loadingDialog.dismiss();
                        Utils.showToastOnMainThread(requireContext(), customMessage);
                    }

                    @Override
                    public void onFailure(String customMessage, Exception exception, String param) {
                        loadingDialog.dismiss();
                        Utils.showToastOnMainThread(requireContext(), customMessage);
                    }
                });

            }
            else
                Utils.showToastOnMainThread(requireContext(),"Memo is Empty");
        });
        return view;
    }

    public void fillDetails(){
        loadingDialog.show();
        dateString = Utils.formatDateString(dateString);
        FirebaseUtils.fetchSingleEvent(mobile, dateString, new FirebaseUtils.Callback<DiaryEventModel, String>() {
            @Override
            public void onSuccess(String customMessage, DiaryEventModel result) {
                description_textView.setText(result.getDescription());
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String customMessage, Exception exception, String param) {
                loadingDialog.dismiss();
            }
        });

    }

    public void init(View view){
        saveButton = view.findViewById(R.id.save_button);
        clearButton = view.findViewById(R.id.clear_button);
        loadingDialog = new LoadingDialog(requireActivity());
        TextView date_textView = view.findViewById(R.id.date_textView);
        description_textView = view.findViewById(R.id.message_edit_text);
        SharedPreferences prefCredentials = requireActivity()
                .getSharedPreferences(AppConstants.PREF_CREDENTIALS, Context.MODE_PRIVATE);
        mobile = prefCredentials.getString(AppConstants.KEY_MOBILE_NUMBER, "");

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
        dateString = formatter1.format(date);
        date_textView.setText(formatter.format(date));
    }
}