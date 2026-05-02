package com.example.mentortrack_teacher;

import com.google.firebase.Timestamp; // ADD THIS IMPORT

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Meeting {
    private String id;
    private String meetingId;
    private String mentorEmail;
    private long scheduledTime;
    private String status;
    private List<String> studentEmails;
    private String attendanceType;
    private Timestamp createdAt; // CHANGE FROM long to Timestamp

    // Default constructor required for Firestore
    public Meeting() {}

    public Meeting(String meetingId, String mentorEmail, long scheduledTime, String status,
                   List<String> studentEmails, String attendanceType) {
        this.meetingId = meetingId;
        this.mentorEmail = mentorEmail;
        this.scheduledTime = scheduledTime;
        this.status = status;
        this.studentEmails = studentEmails;
        this.attendanceType = attendanceType;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }

    public String getMentorEmail() { return mentorEmail; }
    public void setMentorEmail(String mentorEmail) { this.mentorEmail = mentorEmail; }

    public long getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(long scheduledTime) { this.scheduledTime = scheduledTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getStudentEmails() { return studentEmails; }
    public void setStudentEmails(List<String> studentEmails) { this.studentEmails = studentEmails; }

    public String getAttendanceType() { return attendanceType; }
    public void setAttendanceType(String attendanceType) { this.attendanceType = attendanceType; }

    // CHANGED: Use Timestamp instead of long
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    // Helper method to format date
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
        return sdf.format(new Date(scheduledTime));
    }

    // Helper method to get student count
    public int getStudentCount() {
        return studentEmails != null ? studentEmails.size() : 0;
    }
}