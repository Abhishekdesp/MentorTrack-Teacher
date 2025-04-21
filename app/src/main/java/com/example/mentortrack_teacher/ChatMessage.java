package com.example.mentortrack_teacher;

import com.google.firebase.Timestamp;

public class ChatMessage {
    private String message;
    private String sender;
    private Timestamp timestamp;

    public ChatMessage() {
        // Required for Firebase
    }

    public ChatMessage(String message, String sender, Timestamp timestamp) {
        this.message = message;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}