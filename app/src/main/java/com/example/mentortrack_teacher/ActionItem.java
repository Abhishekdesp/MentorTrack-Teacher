package com.example.mentortrack_teacher;

public class ActionItem {
    private String text;
    private String assignee;

    public ActionItem() {}

    public ActionItem(String text, String assignee) {
        this.text = text;
        this.assignee = assignee;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }
}