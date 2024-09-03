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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stressApp.Adapter.FAQAdapter;
import com.example.stressApp.MainFragments.SettingFragment;
import com.example.stressApp.MainFragments.YogaFragment;
import com.example.stressApp.Model.FAQ;
import com.example.stressApp.R;

import java.util.ArrayList;
import java.util.List;

public class HelpFragment extends Fragment {

    private RecyclerView faqRecyclerView;
    private EditText faqSearchBar;
    private FAQAdapter faqAdapter;
    private List<FAQ> faqList = new ArrayList<>();
    private CardView close_button;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        fragmentManager = requireActivity().getSupportFragmentManager();
        close_button = view.findViewById(R.id.close_button_cardView);
        close_button.setOnClickListener(v -> load(new SettingFragment()));

        faqRecyclerView = view.findViewById(R.id.faqRecyclerView);
        faqSearchBar = view.findViewById(R.id.faqSearchBar);

        // Initialize FAQ data
        initializeFAQs();

        // Set up RecyclerView
        faqAdapter = new FAQAdapter(faqList);
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        faqRecyclerView.setAdapter(faqAdapter);

        return view;
    }

    private void load(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }

    private void initializeFAQs() {
        faqList.add(new FAQ("What is the purpose of this app?", "The app helps users manage stress through various techniques."));
        faqList.add(new FAQ("How often should I use the app?", "Daily usage for at least 10-15 minutes is recommended."));
        faqList.add(new FAQ("I am facing issues with the app crashing, what should I do?", "Try restarting your device and ensure you have the latest version."));
        faqList.add(new FAQ("How do I track my stress levels?", "Use the mood tracker under the 'Track' section."));
        faqList.add(new FAQ("Are there any recommended resources?", "Yes, check out the 'Resources' tab for books, podcasts, and articles."));
    }
}
