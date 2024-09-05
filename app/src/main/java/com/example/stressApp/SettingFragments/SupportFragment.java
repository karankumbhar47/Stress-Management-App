package com.example.stressApp.SettingFragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.MainFragments.SettingFragment;
import com.example.stressApp.R;
import com.example.stressApp.Utils.Utils;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SupportFragment extends Fragment {

    private static final String OWNER_EMAIL = "udanvedant@iitbhilai.ac.in"; // Replace with actual email
    private FragmentManager fragmentManager;
    private CardView close_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        fragmentManager = requireActivity().getSupportFragmentManager();
        close_button = view.findViewById(R.id.close_button_cardView);
        close_button.setOnClickListener(v -> load(new SettingFragment()));
        return view;
    }

    private void load(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RatingBar ratingBar = view.findViewById(R.id.feedback_rating);
        EditText feedbackMessage = view.findViewById(R.id.feedback_message);
        Button submitButton = view.findViewById(R.id.submit_feedback);

        submitButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String message = feedbackMessage.getText().toString();
            sendFeedbackEmail(rating, message);
        });
    }

    private void sendFeedbackEmail(float rating, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{OWNER_EMAIL});
        intent.putExtra(Intent.EXTRA_SUBJECT, "App Feedback");
        intent.putExtra(Intent.EXTRA_TEXT, "Rating: " + rating + "\n\nFeedback:\n" + message);

        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Send Feedback"));
        } else {
            Utils.showToastOnMainThread(getActivity(), "No email client installed");
        }
    }
}
