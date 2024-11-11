package com.example.stressApp.HomeFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stressApp.Model.DiaryEventModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.Utils.FirebaseUtils;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;

public class ShowDataFragment extends Fragment {
    private TextView date_textView;
    private TextView description_textView;
    private SharedPreferences prefCredentials;
    private LoadingDialog loadingDialog;

    public ShowDataFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_show_data, container, false);

        prefCredentials = requireActivity().getSharedPreferences(AppConstants.PREF_CREDENTIALS, Context.MODE_PRIVATE);
        loadingDialog = new LoadingDialog(requireActivity());
        date_textView = view.findViewById(R.id.date_textView);
        description_textView = view.findViewById(R.id.description_textView);

        String date =  ShowDataFragmentArgs.fromBundle(getArguments()).getDate();
        String description =  ShowDataFragmentArgs.fromBundle(getArguments()).getDescription();

        if(description==null){
            String mobile = prefCredentials.getString(AppConstants.KEY_MOBILE_NUMBER,"");
            date = Utils.formatDateString(date);
            FirebaseUtils.fetchSingleEvent(mobile, date, new FirebaseUtils.Callback<DiaryEventModel, String>() {
                @Override
                public void onSuccess(String customMessage, DiaryEventModel result) {
                    if(result==null)
                        description_textView.setText("Empty Memo");
                    else
                        description_textView.setText(result.getDescription());
                    loadingDialog.dismiss();
                }

                @Override
                public void onFailure(String customMessage, Exception exception, String param) {
                    description_textView.setText("Empty Memo");
                    loadingDialog.dismiss();
                }
            });
        }

        date_textView.setText(date);
        description_textView.setText(description);
        return view;
    }
}