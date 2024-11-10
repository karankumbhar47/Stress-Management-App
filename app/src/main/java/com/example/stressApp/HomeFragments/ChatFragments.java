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
import com.example.stressApp.MessageAdapter;
import com.example.stressApp.R;

public class ChatFragments extends Fragment {

    private ChatViewModel chatViewModel;
    private MessageAdapter messageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Initialize ViewModel
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.messageList);
        messageAdapter = new MessageAdapter();
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Observe message list
        chatViewModel.getMessageList().observe(getViewLifecycleOwner(), messageAdapter::submitList);

        // Set up send button and input
        EditText messageInput = view.findViewById(R.id.messageInput);
        ImageButton sendButton = view.findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                chatViewModel.sendMessage(message);
                messageInput.setText("");
            }
        });

        return view;
    }
}

