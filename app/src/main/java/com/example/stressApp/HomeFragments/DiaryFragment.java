package com.example.stressApp.HomeFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import com.example.stressApp.MainFragments.YogaFragmentDirections;
import com.example.stressApp.Model.InformationModel;
import com.example.stressApp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DiaryFragment extends Fragment {

    ListView listView;
    SqliteDatabase db;
    ArrayList<InformationModel> arrayList;
    ArrayList<String> selectList = new ArrayList<String>();
    ArrayList<Integer> unDeleteSelect = new ArrayList<Integer>();
    private NavController navController;

    ArrayAdapter arrayAdapter;

    int count = 0;

    public DiaryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        navController = NavHostFragment.findNavController(this);

        db = new SqliteDatabase(this);
        SQLiteDatabase sqliteDatabase = db.getWritableDatabase();

        listView = view.findViewById(R.id.ListviewId);

        arrayList=new ArrayList<InformationModel>();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        // ClickListener for floating action bar
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiaryFragmentDirections.ActionDiaryFragmentToAddDataFragment action = DiaryFragmentDirections.actionDiaryFragmentToAddDataFragment();
                navController.navigate(action);
            }
        });

        view();//calling view method

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DiaryFragmentDirections.actionDiaryFragmentToUpdateActivityFragment()
                Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
                intent.putExtra("subject",arrayList.get(i).getSubject());
                intent.putExtra("description",arrayList.get(i).getDescription());
                intent.putExtra("listId",arrayList.get(i).getId());
                startActivity(intent);
            }
        });
        return view;
    }
}