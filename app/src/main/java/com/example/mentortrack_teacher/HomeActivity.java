package com.example.mentortrack_teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    private CardView cardAllocatedStudents, cardAllStudents;
    private TextView assignedStudentsCount;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get email from SharedPreferences
        sharedPreferences = getSharedPreferences("MentorPrefs", Context.MODE_PRIVATE);
        String currentUserEmail = sharedPreferences.getString("email", null);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MentorTrack");

        // Initialize views
        cardAllocatedStudents = findViewById(R.id.card_allocated_students);
        cardAllStudents = findViewById(R.id.card_all_students);
        assignedStudentsCount = findViewById(R.id.assigned_students_count);

        // Check if user is logged in
        if (currentUserEmail == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Fetch and display counts
        fetchAssignedStudentsCount(currentUserEmail);

        // Set click listeners
        cardAllocatedStudents.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, DashboardActivity.class));
        });

        cardAllStudents.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, AllStudentsActivity.class));
        });
    }

    private void fetchAssignedStudentsCount(String teacherEmail) {
        db.collection("teachers")
                .document(teacherEmail)
                .collection("assignedstudents")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading students", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        int count = value.size();
                        assignedStudentsCount.setText(String.valueOf(count));
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        String currentUserEmail = sharedPreferences.getString("email", null);
        if (currentUserEmail != null) {
            fetchAssignedStudentsCount(currentUserEmail);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_profile) {
            startActivity(new Intent(this, AdminProfileActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}