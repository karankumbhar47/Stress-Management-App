package com.example.stressApp;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<MessageModel> messageList;

    public MessageAdapter(List<MessageModel> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_row, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel messageModel = messageList.get(position);

        // Set message text
        holder.messageText.setText(messageModel.getMessage());

        // Apply styling based on the role
        boolean isModel = messageModel.getRole().equals("model");
        holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(
                isModel ? R.color.model_message_color : R.color.user_message_color));

        // Adjust alignment based on sender
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.cardView.getLayoutParams();
        layoutParams.gravity = isModel ? Gravity.START : Gravity.END;
        holder.cardView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        final TextView messageText;
        final CardView cardView;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            cardView = itemView.findViewById(R.id.messageCard);
        }
    }
}
