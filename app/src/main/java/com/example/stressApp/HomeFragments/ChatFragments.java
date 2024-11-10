package com.example.stressApp.HomeFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stressApp.ChatViewModel;
import com.example.stressApp.Constants;
import com.example.stressApp.MessageAdapter;
import com.example.stressApp.MessageModel;
import com.example.stressApp.R;
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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        generativeModel = new GenerativeModel("gemini-pro", Constants.apiKey);
        modelFutures = GenerativeModelFutures.from(generativeModel);

        RecyclerView recyclerView = view.findViewById(R.id.messageList);
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        EditText messageInput = view.findViewById(R.id.messageInput);
        ImageButton sendButton = view.findViewById(R.id.sendButton);



        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                messageInput.setText("");
            }
        });

        return view;
    }


    private void sendMessage(String question) {
        messageList.add(new MessageModel(question, "user"));
        messageList.add(new MessageModel("Typing...", "model"));
        messageAdapter.submitList(new ArrayList<>(messageList));


        Content content = new Content.Builder().addText(question).build();


        ListenableFuture<GenerateContentResponse> response = modelFutures.generateContent(content);


        Futures.addCallback(
                response,
                new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {

                        messageList.remove(messageList.size() - 1);
                        messageList.add(new MessageModel(result.getText(), "model"));
                        messageAdapter.submitList(new ArrayList<>(messageList));
                    }

                    @Override
                    public void onFailure(Throwable t) {

                        messageList.remove(messageList.size() - 1);
                        messageList.add(new MessageModel("Error: " + t.getMessage(), "model"));
                        messageAdapter.submitList(new ArrayList<>(messageList));
                        t.printStackTrace();
                    }
                },
                executor
        );
    }
}

