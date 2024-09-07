package com.example.stressApp.SettingFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stressApp.Adapter.FAQAdapter;
import com.example.stressApp.MainPage;
import com.example.stressApp.Model.FAQModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class HelpFragment extends Fragment {

    private RecyclerView faqRecyclerView;
    private EditText faqSearchBar;
    private FAQAdapter faqAdapter;
    private List<FAQModel> faqModelList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        faqRecyclerView = view.findViewById(R.id.faqRecyclerView);

        // Initialize FAQ data
        initializeFAQs();

        // Set up RecyclerView
        faqAdapter = new FAQAdapter(faqModelList);
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        faqRecyclerView.setAdapter(faqAdapter);

        return view;
    }

    private void initializeFAQs() {
        faqModelList.add(new FAQModel("What is the purpose of this app?", "The app helps users manage stress through various techniques."));
        faqModelList.add(new FAQModel("How often should I use the app?", "Daily usage for at least 10-15 minutes is recommended."));
        faqModelList.add(new FAQModel("I am facing issues with the app crashing, what should I do?", "Try restarting your device and ensure you have the latest version."));
        faqModelList.add(new FAQModel("How do I track my stress levels?", "Use the mood tracker under the 'Track' section."));
        faqModelList.add(new FAQModel("Are there any recommended resources?", "Yes, check out the 'Resources' tab for books, podcasts, and articles."));
    }
}
