package com.example.stressApp.HomeFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.res.AssetManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.stressApp.Adapter.HabitAdapter;
import com.example.stressApp.Model.HabitModel;
import com.example.stressApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class HabitsFragment extends Fragment {
    private ExpandableListView expandableListView;
    private HabitAdapter habitAdapter;

    public HabitsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habits, container, false);
        expandableListView = view.findViewById(R.id.expandableListView);
        habitAdapter = new HabitAdapter(requireContext(), new ArrayList<>(), new HashMap<>());
        expandableListView.setAdapter(habitAdapter);

        expandableListView.setOnGroupExpandListener(groupPosition -> {
            for (int i = 0; i < habitAdapter.getGroupCount(); i++) {
                if (i != groupPosition) {
                    expandableListView.collapseGroup(i);
                }
            }
        });

        loadHabitsFromJSON();

        return view;
    }


    private void loadHabitsFromJSON() {
        String json = loadJSONFromAsset();
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                List<String> categories = new ArrayList<>();
                HashMap<String, List<HabitModel>> habitDetails = new HashMap<>();

                // Parse the categories (morning, midday, evening, etc.)
                for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                    String category = it.next();
                    categories.add(category);
                    List<HabitModel> habitModels = new ArrayList<>();
                    JSONArray habitsArray = jsonObject.getJSONArray(category);

                    // Parse each habit in the category
                    for (int i = 0; i < habitsArray.length(); i++) {
                        JSONObject habitObject = habitsArray.getJSONObject(i);
                        String habitName = habitObject.getString("habit");
                        String description = habitObject.getString("description");
                        String duration = habitObject.getString("duration");
                        habitModels.add(new HabitModel(habitName, description, duration));
                    }
                    habitDetails.put(category, habitModels);
                }

                habitAdapter.setCategories(categories);
                habitAdapter.setHabitDetails(habitDetails);
                habitAdapter.notifyDataSetChanged();

                expandableListView.expandGroup(0); // Expands the first group by default
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Error parsing habits data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            AssetManager assetManager = requireContext().getAssets();
            InputStream inputStream = assetManager.open("jsonData/habits.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Toast.makeText(getContext(), "Error parsing habits data : "+ex.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
        return json;
    }

}
