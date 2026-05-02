package com.example.mentortrack_teacher;

import com.google.firebase.Timestamp;

public class Attendance {
    private String id;
    private String meetingId;
    private String studentEmail;
    private String status;
    private Timestamp checkInTime;
    private long duration;
    private String dataSource;
    private String syncStatus;
    private Timestamp createdAt;

    // Default constructor required for Firestore
    public Attendance() {}

    public Attendance(String meetingId, String studentEmail, String status,
                      Timestamp checkInTime, long duration, String dataSource, String syncStatus) {
        this.meetingId = meetingId;
        this.studentEmail = studentEmail;
        this.status = status;
        this.checkInTime = checkInTime;
        this.duration = duration;
        this.dataSource = dataSource;
        this.syncStatus = syncStatus;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCheckInTime() { return checkInTime; }
    public void setCheckInTime(Timestamp checkInTime) { this.checkInTime = checkInTime; }

    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }

    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }

    public String getSyncStatus() { return syncStatus; }
    public void setSyncStatus(String syncStatus) { this.syncStatus = syncStatus; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}