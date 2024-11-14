package com.example.stressApp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stressApp.Model.HabitModel;
import com.example.stressApp.R;

import java.util.HashMap;
import java.util.List;

public class HabitAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> categories;
    private HashMap<String, List<HabitModel>> habitDetails;

    public HabitAdapter(Context context, List<String> categories, HashMap<String, List<HabitModel>> habitDetails) {
        this.context = context;
        this.categories = categories;
        this.habitDetails = habitDetails;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setHabitDetails(HashMap<String, List<HabitModel>> habitDetails) {
        this.habitDetails = habitDetails;
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return habitDetails.get(categories.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return habitDetails.get(categories.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, null);
        }

        TextView groupText = convertView.findViewById(R.id.groupText);
        groupText.setText((String) getGroup(groupPosition));

        ImageView arrowIcon = convertView.findViewById(R.id.arrowIcon);
        if (isExpanded)
            arrowIcon.setImageResource(R.drawable.up_arrow);
        else
            arrowIcon.setImageResource(R.drawable.down_arrow);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        HabitModel habitModel = (HabitModel) getChild(groupPosition, childPosition);
        TextView habitName = convertView.findViewById(R.id.habitName);
        TextView description = convertView.findViewById(R.id.description);
        TextView duration = convertView.findViewById(R.id.duration);

        habitName.setText(habitModel.getHabitName());
        description.setText(habitModel.getDescription());
        duration.setText(habitModel.getDuration());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
