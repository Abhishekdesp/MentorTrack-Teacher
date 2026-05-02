package com.example.mentortrack_teacher;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class MeetingPointsDialog extends Dialog {

    private FirefliesMeetingSummary meetingSummary;
    private Context context;

    public MeetingPointsDialog(Context context, FirefliesMeetingSummary meetingSummary) {
        super(context);
        this.context = context;
        this.meetingSummary = meetingSummary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(createDialogLayout());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private LinearLayout createDialogLayout() {
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        mainLayout.setPadding(32, 32, 32, 32);
        mainLayout.setBackgroundColor(Color.WHITE);

        // Title
        TextView title = new TextView(context);
        title.setText(meetingSummary.getMeetingTitle() != null ? meetingSummary.getMeetingTitle() : "Meeting Points");
        title.setTextSize(20);
        title.setTextColor(Color.BLACK);
        title.setPadding(0, 0, 0, 24);
        mainLayout.addView(title);

        // Date
        TextView date = new TextView(context);
        date.setText("📅 " + (meetingSummary.getMeetingDate() != null ? meetingSummary.getMeetingDate() : "Recent Meeting"));
        date.setTextSize(14);
        date.setTextColor(Color.GRAY);
        date.setPadding(0, 0, 0, 16);
        mainLayout.addView(date);

        // Action Items Section
        if (meetingSummary.getActionItems() != null && !meetingSummary.getActionItems().isEmpty()) {
            addSectionTitle(mainLayout, "✅ Action Items");
            for (ActionItem item : meetingSummary.getActionItems()) {
                addActionItemCard(mainLayout, item);
            }
        }

        // Key Tasks Section
        if (meetingSummary.getKeyTasks() != null && !meetingSummary.getKeyTasks().isEmpty()) {
            addSectionTitle(mainLayout, "📋 Key Tasks");
            for (KeyTask task : meetingSummary.getKeyTasks()) {
                addKeyTaskCard(mainLayout, task);
            }
        }

        // Topics Section
        if (meetingSummary.getTopics() != null && !meetingSummary.getTopics().isEmpty()) {
            addSectionTitle(mainLayout, "💬 Topics Discussed");
            for (Topic topic : meetingSummary.getTopics()) {
                addTopicCard(mainLayout, topic);
            }
        }

        // Close Button
        MaterialButton closeButton = new MaterialButton(context);
        closeButton.setText("Close");
        closeButton.setBackgroundColor(context.getResources().getColor(R.color.purple_500));
        closeButton.setTextColor(Color.WHITE);
        closeButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ((LinearLayout.LayoutParams) closeButton.getLayoutParams()).setMargins(0, 24, 0, 0);
        closeButton.setOnClickListener(v -> dismiss());
        mainLayout.addView(closeButton);

        return mainLayout;
    }

    private void addSectionTitle(LinearLayout parent, String titleText) {
        TextView title = new TextView(context);
        title.setText(titleText);
        title.setTextSize(16);
        title.setTextColor(Color.BLACK);
        title.setPadding(0, 16, 0, 8);
        parent.addView(title);
    }

    private void addActionItemCard(LinearLayout parent, ActionItem item) {
        MaterialCardView card = new MaterialCardView(context);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        card.setCardElevation(4);
        card.setRadius(12);
        card.setContentPadding(16, 16, 16, 16);
        ((LinearLayout.LayoutParams) card.getLayoutParams()).setMargins(0, 0, 0, 8);

        LinearLayout cardLayout = new LinearLayout(context);
        cardLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(context);
        textView.setText(item.getText());
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);

        TextView assigneeView = new TextView(context);
        assigneeView.setText("👤 " + (item.getAssignee() != null ? item.getAssignee() : "Not assigned"));
        assigneeView.setTextSize(12);
        assigneeView.setTextColor(Color.GRAY);
        assigneeView.setPadding(0, 4, 0, 0);

        cardLayout.addView(textView);
        cardLayout.addView(assigneeView);
        card.addView(cardLayout);
        parent.addView(card);
    }

    private void addKeyTaskCard(LinearLayout parent, KeyTask task) {
        MaterialCardView card = new MaterialCardView(context);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        card.setCardElevation(4);
        card.setRadius(12);
        card.setContentPadding(16, 16, 16, 16);
        ((LinearLayout.LayoutParams) card.getLayoutParams()).setMargins(0, 0, 0, 8);

        TextView textView = new TextView(context);
        textView.setText("• " + task.getText());
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);

        card.addView(textView);
        parent.addView(card);
    }

    private void addTopicCard(LinearLayout parent, Topic topic) {
        MaterialCardView card = new MaterialCardView(context);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        card.setCardElevation(4);
        card.setRadius(12);
        card.setContentPadding(16, 16, 16, 16);
        ((LinearLayout.LayoutParams) card.getLayoutParams()).setMargins(0, 0, 0, 8);

        TextView textView = new TextView(context);
        textView.setText("💬 " + topic.getText());
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);

        card.addView(textView);
        parent.addView(card);
    }
}