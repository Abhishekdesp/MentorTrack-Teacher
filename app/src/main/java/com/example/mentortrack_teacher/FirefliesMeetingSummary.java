package com.example.mentortrack_teacher;

import java.util.List;

public class FirefliesMeetingSummary {
    private String meetingTitle;
    private String meetingDate;
    private List<ActionItem> actionItems;
    private List<KeyTask> keyTasks;
    private List<Topic> topics;

    public FirefliesMeetingSummary() {}

    public String getMeetingTitle() { return meetingTitle; }
    public void setMeetingTitle(String meetingTitle) { this.meetingTitle = meetingTitle; }

    public String getMeetingDate() { return meetingDate; }
    public void setMeetingDate(String meetingDate) { this.meetingDate = meetingDate; }

    public List<ActionItem> getActionItems() { return actionItems; }
    public void setActionItems(List<ActionItem> actionItems) { this.actionItems = actionItems; }

    public List<KeyTask> getKeyTasks() { return keyTasks; }
    public void setKeyTasks(List<KeyTask> keyTasks) { this.keyTasks = keyTasks; }

    public List<Topic> getTopics() { return topics; }
    public void setTopics(List<Topic> topics) { this.topics = topics; }
}