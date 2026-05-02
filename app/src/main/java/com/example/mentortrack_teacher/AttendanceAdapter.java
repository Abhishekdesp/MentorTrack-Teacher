package com.example.mentortrack_teacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<Attendance> attendanceList;
    private MeetingDetailsActivity context;

    public AttendanceAdapter(MeetingDetailsActivity context, List<Attendance> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Attendance attendance = attendanceList.get(position);

        holder.tvStudentEmail.setText(attendance.getStudentEmail());
        holder.tvStatus.setText("Status: " + attendance.getStatus());
        holder.tvDuration.setText("Duration: " + attendance.getDuration() + " mins");
        holder.tvDataSource.setText("Source: " +
                (attendance.getDataSource() != null ? attendance.getDataSource() : "Manual"));

        // Set text color based on status
        if ("present".equals(attendance.getStatus())) {
            holder.tvStatus.setTextColor(0xFF4CAF50); // Green
        } else {
            holder.tvStatus.setTextColor(0xFFF44336); // Red
        }

        // Show check-in time if available
        if (attendance.getCheckInTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
            String checkInTime = sdf.format(attendance.getCheckInTime().toDate());
            holder.tvCheckInTime.setText("Checked in: " + checkInTime);
        } else {
            holder.tvCheckInTime.setText("Not checked in");
        }
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentEmail, tvStatus, tvCheckInTime, tvDuration, tvDataSource;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStudentEmail = itemView.findViewById(R.id.tvStudentEmail);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCheckInTime = itemView.findViewById(R.id.tvCheckInTime);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvDataSource = itemView.findViewById(R.id.tvDataSource);
        }
    }
}