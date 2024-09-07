package com.example.stressApp.SettingFragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.Adapter.LanguageAdapter;
import com.example.stressApp.MainFragments.SettingFragment;
import com.example.stressApp.R;

import java.util.Arrays;
import java.util.List;

public class LanguageFragment extends Fragment {
    private RecyclerView recyclerView;
    private LanguageAdapter languageAdapter;
    private List<String> list;

    public LanguageFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_language, container, false);

        list = Arrays.asList("English","Hindi","Spanish","French","Russian");

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),3));
        languageAdapter = new LanguageAdapter(requireContext(),list,0);
        recyclerView.setAdapter(languageAdapter);

        return view;
    }

}
