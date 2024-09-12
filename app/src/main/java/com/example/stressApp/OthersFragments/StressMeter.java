package com.example.stressApp.OthersFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.stressApp.Adapter.QuestionAdapter;
import com.example.stressApp.MainFragments.OtherFragment;
import com.example.stressApp.MainFragments.YogaFragmentDirections;
import com.example.stressApp.Model.Question;
import com.example.stressApp.R;
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.Utils.JsonHelper;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;

import java.util.List;

public class StressMeter extends Fragment {
    private List<Question> questions;
    private RecyclerView recyclerView;
    private QuestionAdapter adapter;
    private LoadingDialog loadingDialog;
    private CardView submit_button, close_button;
    private NavController navController;

    public StressMeter() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stress_meter, container, false);

        init(view);
        setAdapter();
//        submit_button.setOnClickListener(v -> countPoints());
//        close_button.setOnClickListener(v -> navController.navigateUp());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FrameLayout submitButtonContainer = requireActivity().findViewById(R.id.custom_submit_button_container);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View submitButton = inflater.inflate(R.layout.button_submit_card, submitButtonContainer, false);

        submitButtonContainer.addView(submitButton);
        submitButton.findViewById(R.id.submit_button_cardView).setOnClickListener(v -> {
            countPoints();
        });
    }

    private void init(View view){
        loadingDialog = new LoadingDialog(requireActivity());
        recyclerView = view.findViewById(R.id.recycler_view_questions);
//        submit_button = view.findViewById(R.id.submit_button_cardView);
//        close_button = view.findViewById(R.id.close_button_cardView);
        navController = NavHostFragment.findNavController(this);
    }

    private void setAdapter(){
        loadingDialog.show();
        JsonHelper.fetchQuestionsFromAssets(requireContext(), new JsonHelper.Callback<List<Question>, String>() {
            @Override
            public void onSuccess(String customMessage, List<Question> result) {
                questions = Utils.getRandomQuestions(result,10);
                adapter = new QuestionAdapter(questions);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String customMessage, Exception exception, String param) {
                Log.e(AppConstants.LOG_ACTIVITY, "onFailure: error while loading question ",exception);
                Utils.showToastOnMainThread(requireContext(),"Unable to load data due to "+customMessage);
                loadingDialog.dismiss();
            }
        });
    }

    private void countPoints(){
        int currentPoints = 0;
        int totalPoints = 0;
        boolean flag = true;

        for (Question question : questions) {
            int selectedOption = question.getSelectedOption();
            if (selectedOption == -1) {
                Utils.showToastOnMainThread(requireContext(), "First Answer All Question");
                flag = false;
                break;
            }
            else{
                currentPoints += question.getOptions().get(selectedOption).getPoints();
                totalPoints += Math.max(
                        question.getOptions().get(0).getPoints(),
                        Math.max( question.getOptions().get(1).getPoints(),
                        Math.max(question.getOptions().get(2).getPoints(),
                                question.getOptions().get(3).getPoints()))
                );
            }
        }

        if(flag){
            double scoreValue =  (10*currentPoints)/((double) totalPoints);
            Utils.showToastOnMainThread(requireContext(), "Your score: " + currentPoints);
            int score = (int)  Math.round(scoreValue);
            StressMeterDirections.ActionStressMeterToStressResult action =
                    StressMeterDirections.actionStressMeterToStressResult(score);
            navController.navigate(action);
        }
    }

}
