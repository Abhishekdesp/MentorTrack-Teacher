package com.example.mentortrack_teacher;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<ChatMessage> messageList;
    private EditText editMessage;
    private Button btnSend;
    private FirebaseFirestore db;
    private String studentEmail;
    private String mentorEmail;
    private String chatDocumentId;
    private TextView studentName;
    private ImageView studentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize views
        studentName = findViewById(R.id.student_name);
        studentImage = findViewById(R.id.student_image);
        recyclerView = findViewById(R.id.recyclerMessages);
        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);

        db = FirebaseFirestore.getInstance();
        studentEmail = getIntent().getStringExtra("email");

        // Get mentor email from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MentorPrefs", MODE_PRIVATE);
        mentorEmail = prefs.getString("email", "");

        // Create the combined document ID
        chatDocumentId = studentEmail + "_" + mentorEmail;

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList, mentorEmail);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load student details for the header
        loadStudentDetails();
        loadMessages();

        btnSend.setOnClickListener(v -> {
            String messageText = editMessage.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessage(messageText);
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStudentDetails() {
        db.collection("students").document(studentEmail)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Set student name
                        String name = documentSnapshot.getString("name");
                        studentName.setText(name);

                        // Set profile image
                        String profileImageBase64 = documentSnapshot.getString("profileImage");
                        if (profileImageBase64 != null && !profileImageBase64.isEmpty()) {
                            try {
                                byte[] decodedString = Base64.decode(profileImageBase64, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                studentImage.setImageBitmap(decodedByte);
                            } catch (Exception e) {
                                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading student details", Toast.LENGTH_SHORT).show();
                });
    }

    // Rest of the methods (loadMessages and sendMessage) remain the same as before
    private void loadMessages() {
        CollectionReference messagesCollection = db.collection("chats")
                .document(chatDocumentId)
                .collection("messages");

        Query messagesQuery = messagesCollection.orderBy("timestamp", Query.Direction.ASCENDING);

        messagesQuery.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Toast.makeText(ChatActivity.this, "Error loading messages", Toast.LENGTH_SHORT).show();
                return;
            }

            if (snapshots != null) {
                messageList.clear();
                for (QueryDocumentSnapshot doc : snapshots) {
                    String message = doc.getString("message");
                    String sender = doc.getString("sender");
                    Timestamp timestamp = doc.getTimestamp("timestamp");
                    ChatMessage chatMessage = new ChatMessage(message, sender, timestamp);
                    messageList.add(chatMessage);
                }
                adapter.notifyDataSetChanged();
                if (!messageList.isEmpty()) {
                    recyclerView.scrollToPosition(messageList.size() - 1);
                }
            }
        });
    }

    private void sendMessage(String messageText) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy:HH:mm", Locale.getDefault());
        String documentId = sdf.format(new Date());

        ChatMessage chatMessage = new ChatMessage(messageText, mentorEmail, Timestamp.now());

        db.collection("chats")
                .document(chatDocumentId)
                .collection("messages")
                .document(documentId)
                .set(chatMessage)
                .addOnSuccessListener(documentReference -> {
                    editMessage.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ChatActivity.this, "Error sending message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}