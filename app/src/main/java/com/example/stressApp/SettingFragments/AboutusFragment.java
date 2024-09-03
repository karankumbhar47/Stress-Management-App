package com.example.stressApp.SettingFragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.MainFragments.SettingFragment;
import com.example.stressApp.MainFragments.YogaFragment;
import com.example.stressApp.R;
import android.content.Intent;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AboutusFragment extends Fragment {

    private CardView close_button;
    private FragmentManager fragmentManager;

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
        fragmentManager = requireActivity().getSupportFragmentManager();

        close_button = view.findViewById(R.id.close_button_cardView);
        close_button.setOnClickListener(v -> load(new SettingFragment()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView leaderContact = view.findViewById(R.id.leader_contact);
        leaderContact.setOnClickListener(v -> sendEmail(LEADER_EMAIL));

        String[] teamNames = {
                "Shobhit Jain",
                "Sandesh Ahire",
                "Palkesh Jain",
                "Sanket Dandge",
                "Vikas Samota",
                "Akshat Arora",
                "Rounak Kamble",
                "Gourav",
                "Karan Kumbhar",
        };
        LinearLayout teamMembersContainer = view.findViewById(R.id.team_members_container);

        for (int i = 0; i < teamNames.length; i++) {
            TextView teamMemberView = new TextView(getContext());
            teamMemberView.setText(teamNames[i]);
            teamMemberView.setTextColor(getResources().getColor(R.color.blue));
            teamMemberView.setTextSize(16);
            int finalI = i;
            teamMemberView.setOnClickListener(v -> sendEmail(TEAM_EMAILS[finalI]));
            teamMembersContainer.addView(teamMemberView);
        }
    }

    private void sendEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    private void load(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }

}