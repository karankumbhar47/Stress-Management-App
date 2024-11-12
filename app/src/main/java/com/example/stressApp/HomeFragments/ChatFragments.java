package com.example.stressApp.HomeFragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stressApp.BuildConfig;
import com.example.stressApp.Constants;
import com.example.stressApp.Adapter.MessageAdapter;
import com.example.stressApp.Model.MessageModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.LoadingDialog;
import com.example.stressApp.Utils.Utils;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatFragments extends Fragment {

    private MessageAdapter messageAdapter;
    private List<MessageModel> messageList = new ArrayList<>();
    private GenerativeModel generativeModel;
    private GenerativeModelFutures modelFutures;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private RecyclerView recyclerView;
    private final String systemPrompt = "You are a compassionate doctor specializing in stress management named Rajkumar. Please provide calming, professional advice to help users manage and reduce their stress effectively. Before recommending anything ask user about his condition and then responsed accordingly. Remember your response should be accurate and concise.";
    private LoadingDialog loadingDialog;
    private Context context;
    private String apiKey;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        init(view);


        messageAdapter = new MessageAdapter(requireContext(),messageList);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        messageList = Utils.getMessages(context);
        messageAdapter.submitList(messageList);
        recyclerView.scrollToPosition(messageList.size() - 1);  // Scroll to the last message

        EditText messageInput = view.findViewById(R.id.messageInput);
        view.findViewById(R.id.sendButton).setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty())
                sendMessage(message);
            messageInput.setText("");
        });

        return view;
    }

    private void init(View view){
        apiKey = BuildConfig.API_KEY;
//        if (apiKey == null || apiKey.isEmpty()) {
//            Log.d("apikey", "init: API_KEY environment variable is not set." );
////            throw new IllegalStateException("API_KEY environment variable is not set.");
//        }
        generativeModel = new GenerativeModel("gemini-pro", apiKey);
        modelFutures = GenerativeModelFutures.from(generativeModel);
        messageList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.messageList);
        context = requireContext();
    }


    private void sendMessage(String question) {
        messageList.add(new MessageModel(question, "user"));
        messageList.add(new MessageModel("Typing...", "model"));
        messageAdapter.submitList(new ArrayList<>(messageList));
        messageAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(messageList.size() - 1);  // Scroll to the bottom


        Content content = new Content.Builder().addText(systemPrompt + "\n\nUser" + question).build();
        ListenableFuture<GenerateContentResponse> response = modelFutures.generateContent(content);
        Futures.addCallback(
                response,
                new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        requireActivity().runOnUiThread(() -> {
                            messageList.remove(messageList.size() - 1);
                            messageList.add(new MessageModel(result.getText(), "model"));
                            messageAdapter.submitList(new ArrayList<>(messageList));
                            messageAdapter.notifyDataSetChanged();
                            recyclerView.smoothScrollToPosition(messageList.size() - 1);
                            Utils.saveMessages(requireContext(), messageList);
                        });
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        requireActivity().runOnUiThread(() -> {
                            messageList.remove(messageList.size() - 1);
                            messageList.add(new MessageModel("Error: " + t.getMessage(), "model"));
                            messageAdapter.submitList(new ArrayList<>(messageList));
                            recyclerView.smoothScrollToPosition(messageList.size() - 1);
                            Utils.saveMessages(requireContext(), messageList);
                        });
                    }
                },
                executor
        );
    }
}

