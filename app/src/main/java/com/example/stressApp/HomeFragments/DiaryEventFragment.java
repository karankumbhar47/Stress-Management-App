package com.example.stressApp.HomeFragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stressApp.Adapter.InformationAdapter;
import com.example.stressApp.Model.DiaryEventModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.Utils.FirebaseUtils;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class DiaryEventFragment extends Fragment {
    private NavController navController;
    private LoadingDialog loadingDialog;
    private RecyclerView eventRecyclerView;
    private SharedPreferences prefCredentials;
    private List<DiaryEventModel> diaryEventModels;
    private InformationAdapter informationAdapter;
    private FloatingActionButton eventAdditionButton;

    public DiaryEventFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        init(view);
        setHasOptionsMenu(true);

        eventAdditionButton.setOnClickListener(v -> navController
                .navigate(DiaryEventFragmentDirections.actionDiaryFragmentToUpdateDiaryFragment()));

        setRecyclerView();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem calendarItem = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Calendar");
        calendarItem.setIcon(R.drawable.baseline_calendar_month_24); // Use your calendar icon here
        calendarItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        calendarItem.setOnMenuItemClickListener(item -> {
            showDatePicker(); // Show DatePickerDialog on click
            return true;
        });
    }


    private void showDatePicker() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    if (selectedYear == year && selectedMonth == month && selectedDay == day) {
                        navController.navigate(DiaryEventFragmentDirections.actionDiaryFragmentToUpdateDiaryFragment());
                        Utils.showToastOnMainThread(requireContext(), "Today's date selected");
                    } else {
                        String selectedDate = selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear;
                        navController.navigate(DiaryEventFragmentDirections.actionDiaryFragmentToShowDataFragment(selectedDate,null));
                        Utils.showToastOnMainThread(requireContext(), "Selected date: " + selectedDate);

                    }
                },
                year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    public void init(View view){
        navController = NavHostFragment.findNavController(this);
        diaryEventModels = new ArrayList<>();
        prefCredentials = requireActivity().getSharedPreferences(AppConstants.PREF_CREDENTIALS, Context.MODE_PRIVATE);
        loadingDialog = new LoadingDialog(requireActivity());
        eventRecyclerView = view.findViewById(R.id.event_recyclerView);
        eventAdditionButton = view.findViewById(R.id.fab);
    }

    public void setRecyclerView() {
        String mobileNumber = prefCredentials.getString(AppConstants.KEY_MOBILE_NUMBER,"");
        loadingDialog.show();
        FirebaseUtils.fetchDiaryEvent(mobileNumber, new FirebaseUtils.Callback<List<DiaryEventModel>, String>() {
            @Override
            public void onSuccess(String customMessage, List<DiaryEventModel> result) {
                Utils.showToastOnMainThread(requireContext(),"Event List Loaded");
                diaryEventModels = result;
                Log.d("Update_Diary", "updateDiaryEvent: size "+diaryEventModels.size());
                informationAdapter = new InformationAdapter(requireContext(), diaryEventModels);
                informationAdapter.setOnItemClickListener(position -> {
                    String date = diaryEventModels.get(position).getDateTime();
                    String description = diaryEventModels.get(position).getDescription();
                    navController.navigate(DiaryEventFragmentDirections
                            .actionDiaryFragmentToShowDataFragment(date,description));
                });
                eventRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,true) );
                eventRecyclerView.setAdapter(informationAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String customMessage, Exception exception, String param) {
                Utils.showToastOnMainThread(requireContext(),customMessage);
                loadingDialog.dismiss();
            }
        });

    }
}