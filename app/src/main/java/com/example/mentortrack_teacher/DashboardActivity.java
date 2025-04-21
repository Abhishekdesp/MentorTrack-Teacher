package com.example.mentortrack_teacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private ArrayList<Student> students;
    private FirebaseFirestore db;
    private MaterialButton btnScheduleMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeUI();
        setupFirestore();
        loadAssignedStudents();
        setupScheduleButton();
    }

    // Initialize all UI components
    private void initializeUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Assigned Students");
        }

        recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        students = new ArrayList<>();
        adapter = new StudentAdapter(this, students);
        recyclerView.setAdapter(adapter);

        btnScheduleMeeting = findViewById(R.id.btnScheduleMeeting);
    }

    // Initialize Firestore instance
    private void setupFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    // Set up click listener for schedule meeting button
    private void setupScheduleButton() {
        btnScheduleMeeting.setOnClickListener(v -> {
            String mentorEmail = getCurrentMentorEmail();
            if (mentorEmail.isEmpty()) {
                showToast("User not logged in");
                return;
            }

            // Query Firestore for assigned students' emails
            db.collection("teachers")
                    .document(mentorEmail)
                    .collection("assignedstudents")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<String> attendeeEmails = new ArrayList<>();

                            // Extract document IDs (student emails) from the collection
                            for (DocumentSnapshot doc : task.getResult()) {
                                attendeeEmails.add(doc.getId()); // Document ID = student email
                            }

                            if (!attendeeEmails.isEmpty()) {
                                scheduleMeeting(attendeeEmails);
                            } else {
                                showToast("No students assigned");
                            }
                        } else {
                            showToast("Failed to load students");
                        }
                    });
        });
    }

    // Create calendar intent with meeting details
    private void scheduleMeeting(List<String> studentEmails) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);

        // Set meeting time (1 hour duration starting next hour)
        Calendar startTime = Calendar.getInstance();
        startTime.add(Calendar.HOUR, 1);
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.HOUR, 2);

        // Configure meeting details
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        intent.putExtra(CalendarContract.Events.TITLE, "Mentorship Meeting");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Scheduled via MentorTrack Teacher App");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Google Meet");

        // Add participants (students + mentor)
        studentEmails.add(getCurrentMentorEmail()); // Include mentor as organizer
        intent.putExtra(Intent.EXTRA_EMAIL, studentEmails.toArray(new String[0]));

        // Launch calendar app if available
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showToast("No calendar app found");
        }
    }

    // Helper method to get current mentor's email
    private String getCurrentMentorEmail() {
        SharedPreferences prefs = getSharedPreferences("MentorPrefs", Context.MODE_PRIVATE);
        return prefs.getString("email", "");
    }

    // Load assigned students into RecyclerView
    private void loadAssignedStudents() {
        String mentorEmail = getCurrentMentorEmail();
        if (mentorEmail.isEmpty()) {
            showToast("User not logged in");
            finish();
            return;
        }

        // Query Firestore for assigned students
        db.collection("teachers")
                .document(mentorEmail)
                .collection("assignedstudents")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    students.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String studentEmail = doc.getId();

                        // Get student details from students collection
                        db.collection("students").document(studentEmail)
                                .get()
                                .addOnSuccessListener(studentDoc -> {
                                    if (studentDoc.exists()) {
                                        Student student = studentDoc.toObject(Student.class);
                                        students.add(student);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> showToast("Failed to load students"));
    }

    // Helper method for showing toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Handle back button in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}