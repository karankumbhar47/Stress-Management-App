package com.example.stressApp;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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

public class ChatViewModel extends ViewModel {

    private final List<MessageModel> messageList = new ArrayList<>();
    private final GenerativeModel generativeModel;
    private final GenerativeModelFutures modelFutures;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public ChatViewModel() {
        generativeModel = new GenerativeModel("gemini-pro", Constants.apiKey);
        modelFutures = GenerativeModelFutures.from(generativeModel);
    }

    public LiveData<List<MessageModel>> getMessageList() {
        return (LiveData<List<MessageModel>>) messageList;
    }

    public void sendMessage(String question) {
        // Add user's question to the message list
        messageList.add(new MessageModel(question, "user"));
        messageList.add(new MessageModel("Typing...", "model"));

        // Create content for the Gemini model
        Content content = new Content.Builder().addText(question).build();

        // Asynchronously request response from Gemini model
        ListenableFuture<GenerateContentResponse> response = modelFutures.generateContent(content);

        // Handle the response asynchronously
        Futures.addCallback(
                response,
                new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        // Remove "Typing..." and add the actual response text to message list
                        messageList.remove(messageList.size() - 1);
                        messageList.add(new MessageModel(result.getText(), "model"));
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // Remove "Typing..." and add an error message
                        messageList.remove(messageList.size() - 1);
                        messageList.add(new MessageModel("Error: " + t.getMessage(), "model"));
                        t.printStackTrace();
                    }
                },
                executor
        );
    }
}