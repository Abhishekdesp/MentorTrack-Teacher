package com.example.mentortrack_teacher;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MeetingDetailsActivity extends AppCompatActivity {

    private TextView tvMeetingTitle, tvNoAttendance;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewAttendance;
    private MaterialButton btnViewMeetingPoints;

    private FirebaseFirestore db;
    private FirefliesService firefliesService;

    private String meetingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);

        initializeUI();
        setupFirestore();
        setupFirefliesService();

        // Get meeting ID from intent
        meetingId = getIntent().getStringExtra("MEETING_ID");
        if (meetingId != null) {
            loadMeetingDetails();
            loadAttendanceRecords();
        } else {
            showToast("No meeting ID provided");
            finish();
        }
    }

    private void initializeUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_meeting_details);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvMeetingTitle = findViewById(R.id.tvMeetingTitle);
        progressBar = findViewById(R.id.progressBar);
        tvNoAttendance = findViewById(R.id.tvNoAttendance);
        recyclerViewAttendance = findViewById(R.id.recyclerViewAttendance);

        // Create and add the meeting points button
        btnViewMeetingPoints = new MaterialButton(this);
        btnViewMeetingPoints.setText("📝 View Meeting Points (Fireflies)");
        btnViewMeetingPoints.setBackgroundColor(getResources().getColor(R.color.purple_500));
        btnViewMeetingPoints.setTextColor(getResources().getColor(android.R.color.white));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(32, 8, 32, 16);
        btnViewMeetingPoints.setLayoutParams(params);

        // Add button to layout after the meeting title
        LinearLayout mainLayout = (LinearLayout) tvMeetingTitle.getParent();
        if (mainLayout != null) {
            int titleIndex = mainLayout.indexOfChild(tvMeetingTitle);
            if (titleIndex != -1) {
                mainLayout.addView(btnViewMeetingPoints, titleIndex + 1);
            } else {
                mainLayout.addView(btnViewMeetingPoints, 1);
            }
        }

        // Setup RecyclerView
        recyclerViewAttendance.setLayoutManager(new LinearLayoutManager(this));
        // Initialize your existing attendance adapter here
        // attendanceAdapter = new YourAttendanceAdapter();
        // recyclerViewAttendance.setAdapter(attendanceAdapter);

        // Set click listener for meeting points button
        btnViewMeetingPoints.setOnClickListener(v -> {
            if (meetingId != null) {
                fetchMeetingPoints(meetingId);
            }
        });
    }

    private void setupFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void setupFirefliesService() {
        firefliesService = new FirefliesService();
    }

    private void loadMeetingDetails() {
        db.collection("meetings").document(meetingId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String title = documentSnapshot.getString("title");
                        if (title != null) {
                            tvMeetingTitle.setText(title);
                        } else {
                            tvMeetingTitle.setText("Meeting: " + meetingId);
                        }
                    }
                })
                .addOnFailureListener(e -> showToast("Failed to load meeting details"));
    }

    private void loadAttendanceRecords() {
        showProgress(true);

        db.collection("attendance")
                .whereEqualTo("meetingId", meetingId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    showProgress(false);

                    if (queryDocumentSnapshots.isEmpty()) {
                        tvNoAttendance.setVisibility(View.VISIBLE);
                        recyclerViewAttendance.setVisibility(View.GONE);
                    } else {
                        tvNoAttendance.setVisibility(View.GONE);
                        recyclerViewAttendance.setVisibility(View.VISIBLE);

                        // Process attendance records
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        showToast("Loaded " + documents.size() + " attendance records");
                        // Update your existing RecyclerView adapter here
                    }
                })
                .addOnFailureListener(e -> {
                    showProgress(false);
                    showToast("Failed to load attendance records: " + e.getMessage());
                });
    }

    private void fetchMeetingPoints(String meetingId) {
        showProgress(true);
        btnViewMeetingPoints.setEnabled(false);
        btnViewMeetingPoints.setText("🔄 Loading Meeting Points...");

        firefliesService.getMeetingSummary(meetingId, new FirefliesService.MeetingSummaryCallback() {
            @Override
            public void onSuccess(FirefliesMeetingSummary meetingSummary) {
                showProgress(false);
                btnViewMeetingPoints.setEnabled(true);
                btnViewMeetingPoints.setText("📝 View Meeting Points (Fireflies)");
                showMeetingPointsDialog(meetingSummary);
            }

            @Override
            public void onError(String error) {
                showProgress(false);
                btnViewMeetingPoints.setEnabled(true);
                btnViewMeetingPoints.setText("📝 View Meeting Points (Fireflies)");
                showToast("Failed to load meeting points: " + error);
            }
        });
    }

    private void showMeetingPointsDialog(FirefliesMeetingSummary meetingSummary) {
        MeetingPointsDialog dialog = new MeetingPointsDialog(this, meetingSummary);
        dialog.show();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            recyclerViewAttendance.setVisibility(View.GONE);
            tvNoAttendance.setVisibility(View.GONE);
        } else {
            recyclerViewAttendance.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}