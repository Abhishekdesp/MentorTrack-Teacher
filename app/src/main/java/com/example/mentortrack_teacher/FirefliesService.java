package com.example.mentortrack_teacher;

import android.os.Handler;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FirefliesService {

    public void getMeetingSummary(String meetingId, MeetingSummaryCallback callback) {
        // Simulate API call delay (2 seconds)
        new Handler().postDelayed(() -> {
            try {
                // Randomly decide if we show success or error (90% success rate)
                if (new Random().nextInt(10) < 9) { // 90% success
                    FirefliesMeetingSummary mockSummary = createMockMeetingData(meetingId);
                    callback.onSuccess(mockSummary);
                } else {
                    // 10% chance of error to simulate real API behavior
                    callback.onError("Fireflies API temporarily unavailable. Please try again.");
                }
            } catch (Exception e) {
                callback.onError("Mock data error: " + e.getMessage());
            }
        }, 2000); // 2 second delay
    }

    private FirefliesMeetingSummary createMockMeetingData(String meetingId) {
        FirefliesMeetingSummary summary = new FirefliesMeetingSummary();

        // Set meeting details based on meeting ID
        summary.setMeetingTitle(getMeetingTitle(meetingId));
        summary.setMeetingDate("2024-01-07");

        // Generate random mock data
        summary.setActionItems(generateActionItems());
        summary.setKeyTasks(generateKeyTasks());
        summary.setTopics(generateTopics());

        return summary;
    }

    private String getMeetingTitle(String meetingId) {
        // Generate different titles based on meeting ID
        if (meetingId.contains("weekly")) {
            return "Weekly Progress Review - " + meetingId;
        } else if (meetingId.contains("project")) {
            return "Project Discussion - " + meetingId;
        } else if (meetingId.contains("technical")) {
            return "Technical Guidance Session - " + meetingId;
        } else {
            return "Mentorship Meeting - " + meetingId;
        }
    }

    private List<ActionItem> generateActionItems() {
        return Arrays.asList(
                new ActionItem("Complete project documentation by next Friday", "student1@college.edu"),
                new ActionItem("Prepare presentation slides for demo day", "student2@college.edu"),
                new ActionItem("Review and provide feedback on code submissions", "mentor@college.edu"),
                new ActionItem("Set up development environment with required tools", "student3@college.edu"),
                new ActionItem("Research and compare different API architectures", "student1@college.edu")
        );
    }

    private List<KeyTask> generateKeyTasks() {
        return Arrays.asList(
                new KeyTask("Complete authentication module implementation"),
                new KeyTask("Write unit tests for core functionality"),
                new KeyTask("Design and implement database schema"),
                new KeyTask("Create user interface mockups"),
                new KeyTask("Prepare deployment documentation")
        );
    }

    private List<Topic> generateTopics() {
        return Arrays.asList(
                new Topic("Project timeline and milestones"),
                new Topic("Technical challenges and solutions"),
                new Topic("Code review and best practices"),
                new Topic("Upcoming deadlines and deliverables"),
                new Topic("Career guidance and internship opportunities"),
                new Topic("Team collaboration and communication")
        );
    }

    public interface MeetingSummaryCallback {
        void onSuccess(FirefliesMeetingSummary meetingSummary);
        void onError(String error);
    }
}