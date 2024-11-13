package com.example.stressApp.HomeFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stressApp.Model.ChallengeModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.JsonHelper;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;

import java.util.List;

public class DailyChallangeFragment extends Fragment {
    private List<ChallengeModel> challenges;
    private LoadingDialog loadingDialog;
    public DailyChallangeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_challange, container, false);
        loadingDialog = new LoadingDialog(requireActivity());

        loadingDialog.show();
        JsonHelper.getChallengesFromJson(requireContext(), new JsonHelper.Callback<List<ChallengeModel>, String>() {
            @Override
            public void onSuccess(String customMessage, List<ChallengeModel> result) {
                int index = Utils.getDailyRandomNumber(result.size()-1);
                displayChallenge(view, result.get(index));
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String customMessage, Exception exception, String param) {
                Utils.showToastOnMainThread(requireContext(),customMessage);
                loadingDialog.dismiss();
            }
        });
        return view;
    }


    private void displayChallenge(View view, ChallengeModel challenge) {
        ((TextView) view.findViewById(R.id.tvChallengeName)).setText(challenge.getName());
        ((TextView) view.findViewById(R.id.tvShortDescription)).setText(challenge.getDescription());
        ((TextView) view.findViewById(R.id.tvDuration)).setText("Duration: " + challenge.getDuration());
        ((TextView) view.findViewById(R.id.tvDifficultyLevel)).setText("Difficulty: " + challenge.getDifficultyLevel());
        ((TextView) view.findViewById(R.id.tvMotivationalQuote)).setText("\"" + challenge.getMotivationalQuote() + "\"");

        // Populate steps
        LinearLayout stepsContainer = view.findViewById(R.id.stepsContainer);
        stepsContainer.removeAllViews();
        for (String step : challenge.getSteps()) {
            TextView stepTextView = new TextView(requireContext());
            stepTextView.setText("• " + step);
            stepTextView.setTextSize(16);
            stepTextView.setPadding(0, 4, 0, 4);
            stepsContainer.addView(stepTextView);
        }

        // Populate tips
        LinearLayout tipsContainer = view.findViewById(R.id.tipsContainer);
        tipsContainer.removeAllViews();
        for (String tip : challenge.getTips()) {
            TextView tipTextView = new TextView(requireContext());
            tipTextView.setText("• " + tip);
            tipTextView.setTextSize(16);
            tipTextView.setPadding(0, 4, 0, 4);
            tipsContainer.addView(tipTextView);
        }
    }
}