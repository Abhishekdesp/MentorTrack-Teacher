package com.example.mentortrack_teacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MENTOR = 1;
    private static final int VIEW_TYPE_STUDENT = 2;

    private List<ChatMessage> messageList;
    private String currentUserEmail;

    public MessageAdapter(List<ChatMessage> messageList, String currentUserEmail) {
        this.messageList = messageList;
        this.currentUserEmail = currentUserEmail;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageList.get(position);
        return message.getSender().equals(currentUserEmail) ? VIEW_TYPE_MENTOR : VIEW_TYPE_STUDENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MENTOR) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_mentor, parent, false);
            return new MentorViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_student, parent, false);
            return new StudentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        if (holder instanceof MentorViewHolder) {
            ((MentorViewHolder) holder).textView.setText(message.getMessage());
        } else {
            ((StudentViewHolder) holder).textView.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MentorViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        MentorViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_message);
        }
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_message);
        }
    }
}