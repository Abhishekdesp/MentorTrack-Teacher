package com.example.mentortrack_teacher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    ArrayList<Student> students;
    Context context;

    public StudentAdapter(Context context, ArrayList<Student> students) {
        this.context = context;
        this.students = students;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student s = students.get(position);
        holder.name.setText(s.name);
        holder.email.setText(s.email);

        // Decode Base64 profile image and set it to the ImageView
        if (s.profileImage != null && !s.profileImage.isEmpty()) {
            try {
                byte[] decodedBytes = Base64.decode(s.profileImage, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                holder.profileImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                // Optional: set a fallback/default image
                holder.profileImageView.setImageResource(R.drawable.ic_student);
            }
        } else {
            // No image: show default icon
            holder.profileImageView.setImageResource(R.drawable.ic_student);
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, StudentProfileActivity.class);
            intent.putExtra("email", s.email);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView name, email;
        ImageView profileImageView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            email = itemView.findViewById(R.id.textViewSubject);
            profileImageView = itemView.findViewById(R.id.imageViewIcon);
        }
    }
}
