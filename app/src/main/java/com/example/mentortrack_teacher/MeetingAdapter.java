package com.example.mentortrack_teacher;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    private List<Meeting> meetingsList;
    private AttendanceHistoryActivity context;
    private FirefliesService firefliesService;

    public MeetingAdapter(AttendanceHistoryActivity context, List<Meeting> meetingsList) {
        this.context = context;
        this.meetingsList = meetingsList;
        this.firefliesService = new FirefliesService();
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting, parent, false);
        return new MeetingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        Meeting meeting = meetingsList.get(position);

        holder.tvMeetingId.setText("Meeting: " + meeting.getMeetingId());
        holder.tvDateTime.setText(meeting.getFormattedDate());
        holder.tvStudentCount.setText("Students: " + meeting.getStudentCount());
        holder.tvAttendanceType.setText("Type: " +
                (meeting.getAttendanceType() != null ? meeting.getAttendanceType() : "Manual"));
        holder.tvStatus.setText("Status: " + meeting.getStatus());

        // 🔥 NEW: Fireflies button click listener
        holder.btnViewMeetingPoints.setOnClickListener(v -> {
            // Show loading toast
            Toast.makeText(context, "🔄 Loading meeting points...", Toast.LENGTH_SHORT).show();

            // Fetch meeting points from Fireflies
            fetchMeetingPoints(meeting.getMeetingId(), holder);
        });

        // Make entire item clickable for opening meeting details
        holder.itemView.setOnClickListener(v -> {
            // Open meeting details activity
            Intent intent = new Intent(context, MeetingDetailsActivity.class);
            intent.putExtra("MEETING_ID", meeting.getMeetingId());
            context.startActivity(intent);
        });

        // Set background color based on status
        switch (meeting.getStatus().toLowerCase()) {
            case "completed":
                holder.itemView.setBackgroundColor(0xFFE8F5E8); // Light green
                break;
            case "scheduled":
                holder.itemView.setBackgroundColor(0xFFE3F2FD); // Light blue
                break;
            case "cancelled":
                holder.itemView.setBackgroundColor(0xFFFFEBEE); // Light red
                break;
            default:
                holder.itemView.setBackgroundColor(0xFFFFFFFF); // White
        }
    }

    // 🔥 NEW: Method to fetch meeting points
    private void fetchMeetingPoints(String meetingId, MeetingViewHolder holder) {
        // Disable button during loading
        holder.btnViewMeetingPoints.setEnabled(false);
        holder.btnViewMeetingPoints.setText("Loading...");

        firefliesService.getMeetingSummary(meetingId, new FirefliesService.MeetingSummaryCallback() {
            @Override
            public void onSuccess(FirefliesMeetingSummary meetingSummary) {
                // Re-enable button
                holder.btnViewMeetingPoints.setEnabled(true);
                holder.btnViewMeetingPoints.setText("📝 View Meeting Points");

                // Show meeting points dialog
                MeetingPointsDialog dialog = new MeetingPointsDialog(context, meetingSummary);
                dialog.show();
            }

            @Override
            public void onError(String error) {
                // Re-enable button
                holder.btnViewMeetingPoints.setEnabled(true);
                holder.btnViewMeetingPoints.setText("📝 View Meeting Points");

                // Show error message
                Toast.makeText(context, "❌ " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return meetingsList.size();
    }

    public static class MeetingViewHolder extends RecyclerView.ViewHolder {
        TextView tvMeetingId, tvDateTime, tvStudentCount, tvAttendanceType, tvStatus;
        MaterialButton btnViewMeetingPoints; // 🔥 NEW: Fireflies button

        public MeetingViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMeetingId = itemView.findViewById(R.id.tvMeetingId);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvStudentCount = itemView.findViewById(R.id.tvStudentCount);
            tvAttendanceType = itemView.findViewById(R.id.tvAttendanceType);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnViewMeetingPoints = itemView.findViewById(R.id.btnViewMeetingPoints); // 🔥 NEW
        }
    }
}