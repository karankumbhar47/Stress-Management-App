package com.example.stressApp;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stressApp.Utils.FirebaseUtils;
import com.example.stressApp.Utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YogaDetails extends Fragment {
    private final int yoga_id;
    private YogaModel yogaModel;
    private TextView info, name, title_streches, info_tips;
    private TextView info_streches, title_help, info_help;
    private TextView title_how_to_do, info_how_to_do, title_tips;

    public YogaDetails(int id) {
        this.yoga_id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yoga_details, container, false);
        init(view);

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
        title_streches = view.findViewById(R.id.title_streches);
        info_streches = view.findViewById(R.id.info_streches);
        title_help = view.findViewById(R.id.title_help);
        info_help = view.findViewById(R.id.info_help);

        title_how_to_do = view.findViewById(R.id.title_how_to_do);
        info_how_to_do = view.findViewById(R.id.info_how_to_do);

        title_tips = view.findViewById(R.id.title_tips);
        info_tips = view.findViewById(R.id.info_tips);
    }

    private void setInfo(YogaModel yogaModel){
        Pattern pattern = Pattern.compile("^(.*?)\\s*\\(([^)]+)\\)\\s*(.*)$");
        Matcher matcher = pattern.matcher(yogaModel.getName());

        if (matcher.find()) {
            String textOutsideBrackets = matcher.group(1) + matcher.group(3);
            String textInsideBrackets = matcher.group(2);
            String name = textInsideBrackets + "(" + textOutsideBrackets + ")";
            SpannableString spannableString = new SpannableString(name);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, textInsideBrackets.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            info.setText(spannableString);
        }
        else info.setText(yogaModel.getName());

        info_streches.setText(yogaModel.getStretchedPart());
        info_how_to_do.setText(yogaModel.getHowToDo().toString());
        info_help.setText(yogaModel.getStretchedPart());
        info_tips.setText(yogaModel.getTips().toString());
    }

}