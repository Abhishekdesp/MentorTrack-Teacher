package com.example.mentortrack_teacher;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AllStudentAdapter extends RecyclerView.Adapter<AllStudentAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Student> studentList;
    private List<Student> studentListFull;

    public AllStudentAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = new ArrayList<>(studentList);
        this.studentListFull = new ArrayList<>(studentList);
    }

    public void updateFullList(List<Student> newList) {
        studentListFull.clear();
        studentListFull.addAll(newList);
        studentList.clear();
        studentList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_student_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.txtStudentName.setText(student.name != null ? student.name : "Unnamed");

        holder.btnFeedback.setOnClickListener(v -> {
            if (student.email != null) {
                Intent intent = new Intent(context, SendMessageActivity.class);
                intent.putExtra("studentEmail", student.email);
                intent.putExtra("studentName", student.name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtStudentName;
        ImageButton btnFeedback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStudentName = itemView.findViewById(R.id.txtStudentName);
            btnFeedback = itemView.findViewById(R.id.btnFeedback);
        }
    }

    @Override
    public Filter getFilter() {
        return studentFilter;
    }

    private final Filter studentFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Student> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(studentListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Student student : studentListFull) {
                    if (student.name != null && student.name.toLowerCase().contains(filterPattern)) {
                        filteredList.add(student);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            studentList.clear();
            studentList.addAll((List<Student>) results.values);
            notifyDataSetChanged();
        }
    };
}
