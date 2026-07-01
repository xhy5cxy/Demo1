package com.tour.app.ai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tour.app.R;
import com.tour.app.ai.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatMessage> messageList;

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        
        if (message.isUser()) {
            // 用户消息
            holder.userLayout.setVisibility(View.VISIBLE);
            holder.aiLayout.setVisibility(View.GONE);
            holder.userMessage.setText(message.getContent());
        } else {
            // AI消息
            holder.userLayout.setVisibility(View.GONE);
            holder.aiLayout.setVisibility(View.VISIBLE);
            holder.aiMessage.setText(message.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addMessage(ChatMessage message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    public void clearMessages() {
        int size = messageList.size();
        messageList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout userLayout;
        LinearLayout aiLayout;
        TextView userMessage;
        TextView aiMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userLayout = itemView.findViewById(R.id.layout_user_message);
            aiLayout = itemView.findViewById(R.id.layout_ai_message);
            userMessage = itemView.findViewById(R.id.tv_user_message);
            aiMessage = itemView.findViewById(R.id.tv_ai_message);
        }
    }
}