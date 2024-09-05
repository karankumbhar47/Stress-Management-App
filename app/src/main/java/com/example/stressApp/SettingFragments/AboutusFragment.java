package com.example.stressApp.SettingFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.MainFragments.SettingFragment;
import com.example.stressApp.Model.StudentModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.JsonHelper;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;

import android.content.Intent;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.Objects;

public class AboutusFragment extends Fragment {
    private List<StudentModel> studentModelList;
    private FragmentManager fragmentManager;
    private LoadingDialog loadingDialog;

    private static final String[] TEAM_EMAILS = {
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "karansunilk@iitbhilai.ac.in"
    };
    private static final String LEADER_EMAIL = "udanvedant@iitbhilai.ac.in";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_aboutus, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        fragmentManager = requireActivity().getSupportFragmentManager();
        loadingDialog = new LoadingDialog(requireActivity());
        view.findViewById(R.id.close_button_cardView).setOnClickListener(v -> load(new SettingFragment()));

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

                teamMemberView.setText(String.format("%s(%s)", studentModel.getName(), studentModel.getIdNumber()));
                teamMemberView.setTextColor(getResources().getColor(R.color.blue));
                teamMemberView.setTextSize(16);
                teamMemberView.setOnClickListener(v -> sendEmail(studentModel.getEmail()));
                teamMembersContainer.addView(teamMemberView);
            }
            else{
                leaderContact.setText(String.format( "Leader: %s\nEmail  : %s\nPhone: %s",
                        studentModel.getName(),studentModel.getEmail(),studentModel.getPhoneNumber()));
                leaderContact.setOnClickListener(v -> sendEmail(LEADER_EMAIL));
            }
        }
    }

    private void sendEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        startActivity(Intent.createChooser(intent, "Send Email"));
        Utils.showToastOnMainThread(requireContext(),"Mail sent to "+email);
    }

    private void load(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.commit();
    }

}