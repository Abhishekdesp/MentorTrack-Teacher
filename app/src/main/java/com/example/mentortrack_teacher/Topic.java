package com.example.mentortrack_teacher;

public class Topic {
    private String text;

    public Topic() {}

    public Topic(String text) {
        this.text = text;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}