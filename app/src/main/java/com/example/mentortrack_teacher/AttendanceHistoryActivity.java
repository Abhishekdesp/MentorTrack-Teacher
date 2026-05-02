package com.example.mentortrack_teacher;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMeetings;
    private MeetingAdapter meetingAdapter;
    private List<Meeting> meetingsList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private TextView tvNoMeetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        initializeUI();
        loadMeetingsHistory();
    }

    private void initializeUI() {
        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_attendance);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Attendance History");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        recyclerViewMeetings = findViewById(R.id.recyclerViewMeetings);
        progressBar = findViewById(R.id.progressBar);
        tvNoMeetings = findViewById(R.id.tvNoMeetings);

        // Setup RecyclerView
        recyclerViewMeetings.setLayoutManager(new LinearLayoutManager(this));
        meetingsList = new ArrayList<>();
        meetingAdapter = new MeetingAdapter(this, meetingsList);
        recyclerViewMeetings.setAdapter(meetingAdapter);

        db = FirebaseFirestore.getInstance();
    }

    private void loadMeetingsHistory() {
        String mentorEmail = getCurrentMentorEmail();

        if (mentorEmail.isEmpty()) {
            showToast("User not logged in");
            finish();
            return;
        }

        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        tvNoMeetings.setVisibility(View.GONE);
        recyclerViewMeetings.setVisibility(View.GONE);

        db.collection("meetings")
                .whereEqualTo("mentorEmail", mentorEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);

                    meetingsList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Meeting meeting = document.toObject(Meeting.class);
                        meeting.setId(document.getId());
                        meetingsList.add(meeting);
                    }

                    meetingAdapter.notifyDataSetChanged();

                    if (meetingsList.isEmpty()) {
                        tvNoMeetings.setVisibility(View.VISIBLE);
                        recyclerViewMeetings.setVisibility(View.GONE);
                    } else {
                        tvNoMeetings.setVisibility(View.GONE);
                        recyclerViewMeetings.setVisibility(View.VISIBLE);
                        showToast("Loaded " + meetingsList.size() + " meetings");
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    showToast("Failed to load meetings: " + e.getMessage());
                    tvNoMeetings.setVisibility(View.VISIBLE);
                    tvNoMeetings.setText("Failed to load meetings");
                });
    }

    private String getCurrentMentorEmail() {
        SharedPreferences prefs = getSharedPreferences("MentorPrefs", MODE_PRIVATE);
        return prefs.getString("email", "");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}