package com.example.stressApp.SettingFragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.MainPage;
import com.example.stressApp.Model.StudentModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.Utils.JsonHelper;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;

import android.content.Intent;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class AboutusFragment extends Fragment {
    private List<StudentModel> studentModelList;
    private LoadingDialog loadingDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_aboutus, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        loadingDialog = new LoadingDialog(requireActivity());

        loadingDialog.show();
        JsonHelper.getStudentList(requireContext(), new JsonHelper.Callback<List<StudentModel>, String>() {
            @Override
            public void onSuccess(String customMessage, List<StudentModel> result) {
                loadingDialog.dismiss();
                studentModelList = result;
            }

            @Override
            public void onFailure(String customMessage, Exception exception, String param) {
                loadingDialog.dismiss();
                Utils.showToastOnMainThread(requireContext(),"Failed to load data");
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView leaderContact = view.findViewById(R.id.leader_contact);
        LinearLayout teamMembersContainer = view.findViewById(R.id.team_members_container);

        for (StudentModel studentModel: studentModelList) {
            if(!Objects.equals(studentModel.getRole(), "Leader")){
                TextView teamMemberView = new TextView(getContext());

                teamMemberView.setText(android.text.Html.fromHtml(String.format("<b>%s</b> (%s)", studentModel.getName(), studentModel.getIdNumber())));
                teamMemberView.setTextColor(ContextCompat.getColor(getContext(), R.color.mid_dark_blue));


                teamMemberView.setTextSize(16);
                teamMemberView.setOnClickListener(v -> sendEmail(studentModel.getEmail()));
                teamMembersContainer.addView(teamMemberView);
            }
            else{
                leaderContact.setText(String.format( "Leader: %s\nEmail  : %s\nPhone: %s",
                        studentModel.getName(),studentModel.getEmail(),studentModel.getPhoneNumber()));
                leaderContact.setOnClickListener(v -> sendEmail(studentModel.getEmail()));
            }
        }
    }

    private void sendEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        startActivity(Intent.createChooser(intent, "Send Email"));
        Utils.showToastOnMainThread(requireContext(),"Mail sent to "+email);
    }

}