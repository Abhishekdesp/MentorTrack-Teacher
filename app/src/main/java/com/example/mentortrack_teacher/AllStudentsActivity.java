package com.example.mentortrack_teacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AllStudentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AllStudentAdapter adapter;
    private ArrayList<Student> studentList;
    private FirebaseFirestore db;
    private SearchView searchView;
    private String department = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_students);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(AllStudentsActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerViewAllStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentList = new ArrayList<>();
        adapter = new AllStudentAdapter(this, studentList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadStudentsFromSameDepartment();

        setupSearch();
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void loadStudentsFromSameDepartment() {
        SharedPreferences prefs = getSharedPreferences("MentorPrefs", Context.MODE_PRIVATE);
        String mentorEmail = prefs.getString("email", null);

        if (mentorEmail == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("teachers").document(mentorEmail).get()
                .addOnSuccessListener(teacherDoc -> {
                    if (teacherDoc.exists()) {
                        department = teacherDoc.getString("department");

                        db.collection("students").get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    studentList.clear();
                                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                        String studentDept = doc.getString("department");
                                        if (department != null && department.equals(studentDept)) {
                                            Student student = doc.toObject(Student.class);
                                            if (student != null) {
                                                studentList.add(student);
                                            }
                                        }
                                    }
                                    adapter.updateFullList(studentList);
                                    adapter.notifyDataSetChanged();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to load students", Toast.LENGTH_SHORT).show()
                                );
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load teacher details", Toast.LENGTH_SHORT).show()
                );
    }
}
