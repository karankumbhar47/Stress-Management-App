package com.example.stressApp.YogaFragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stressApp.MainFragments.YogaFragment;
import com.example.stressApp.MainPage;
import com.example.stressApp.Model.YogaModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.Utils.FirebaseUtils;
import com.example.stressApp.Utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YogaDetails extends Fragment {
    private int yoga_id;
    private YogaModel yogaModel;
    private TextView info, name, title_streches, info_tips;
    private TextView info_streches, title_help, info_help;
    private TextView title_how_to_do, info_how_to_do, title_tips;
    private TextView yoga_pose_name, info_name;
    private ImageView picImage;

    public YogaDetails() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yoga_details, container, false);
        init(view);

        yoga_id = YogaDetailsArgs.fromBundle(getArguments()).getYogaId()+1;
        FirebaseUtils.getYogaModel(yoga_id, new FirebaseUtils.Callback<YogaModel, String>() {
            @Override
            public void onSuccess(String customMessage, YogaModel result) {
                yogaModel = result;
                setInfo(yogaModel);
            }

            @Override
            public void onFailure(String customMessage, Exception exception, String param) {
                Utils.showToastOnMainThread(requireContext(),"No Data Loaded");
            }
        });
        return view;
    }

    private void init(View view){
        name = view.findViewById(R.id.name);
        info = view.findViewById(R.id.info);
        picImage = view.findViewById(R.id.yoga_pic);
        info_name = view.findViewById(R.id.name_info);
        title_tips = view.findViewById(R.id.title_tips);
        info_tips = view.findViewById(R.id.info_tips);
        title_help = view.findViewById(R.id.title_help);
        info_help = view.findViewById(R.id.info_help);
        info_streches = view.findViewById(R.id.info_streches);
        title_streches = view.findViewById(R.id.title_streches);
        yoga_pose_name = view.findViewById(R.id.yoga_pose_name);
        title_how_to_do = view.findViewById(R.id.title_how_to_do);
        info_how_to_do = view.findViewById(R.id.info_how_to_do);
    }

    private void setInfo(YogaModel yogaModel) {
        String mainName = yogaModel.getName();
        String sideName = yogaModel.getName();

        Pattern pattern = Pattern.compile("^(.*?)\\s*\\(([^)]+)\\)\\s*(.*)$");
        Matcher matcher = pattern.matcher(yogaModel.getName());
        if (matcher.find()) {
            sideName = matcher.group(1) + matcher.group(3);
            mainName = matcher.group(2);
        }

        ((MainPage) requireActivity()).setToolbarTitle(mainName);
        name.setText(mainName);
        info_name.setText(String.format("(%s)", sideName));

        info.setText(String.format("\t\t\t\t%s", yogaModel.getInfo()));

        title_help.setText(String.format("%s helps with", mainName));
        info_help.setText(String.format("\t\t%s", yogaModel.getHelp()));

        title_streches.setText(String.format("%s stretches", mainName));
        info_streches.setText(String.format("\t\t%s", yogaModel.getStretchedPart()));

        title_how_to_do.setText(String.format("How to do %s", sideName));

        StringBuilder howToDo = new StringBuilder();
        for (String step : yogaModel.getHowToDo()) {
            howToDo.append(" •  ").append(step).append("\n\n");
        }
        info_how_to_do.setText(howToDo.toString());

        title_tips.setText(R.string.tips);
        StringBuilder tips = new StringBuilder();
        for (String step : yogaModel.getTips()) {
            tips.append(" •  ").append(step).append("\n\n");
        }
        info_tips.setText(tips.toString());


        Integer drawableId = AppConstants.drawableMap.get(yogaModel.getPath());
        if (drawableId != null) {
            Glide.with(requireContext()).load(drawableId).into(picImage);
            Log.d(AppConstants.LOG_YOGA, "onBindViewHolder: drawable id " + drawableId);
        }
    }

}