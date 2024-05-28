package com.example.task10_1d;


import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<HistoryItem> historyList;

    // 构造函数
    public HistoryAdapter(List<HistoryItem> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem historyItem = historyList.get(position);

        holder.quizNumberTextView.setText("Quiz: " + historyItem.getQuizNumber());
        holder.topicTextView.setText("Topic: " + historyItem.getTopic());
        holder.markTextView.setText("Marks: " + historyItem.getMark());

        // Clear existing content
        holder.linearLayoutResults.removeAllViews();

        // Get the context
        Context context = holder.itemView.getContext();


        for (String question : historyItem.getQuestions()) {
            LinearLayout cardLayout = new LinearLayout(context);
            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            cardLayout.setBackgroundResource(R.drawable.edit_text_border);
            cardLayout.setPadding(16, 16, 16, 16);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cardLayout.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, dpToPx(context, 5));
            cardLayout.setLayoutParams(layoutParams);

            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            SpannableString spannableString = new SpannableString(question);

            int startIndex = question.indexOf("Correct Answer:");
            if (startIndex != -1) {
                int endIndex = question.length();
                if (question.contains("(You are not correct)")) {
                    spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.red)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (question.contains("(You are correct)")) {
                    spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.green)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            textView.setText(spannableString);
            cardLayout.addView(textView);
            holder.linearLayoutResults.addView(cardLayout);
        }
    }

    private int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView quizNumberTextView;
        TextView topicTextView;
        TextView markTextView;
        LinearLayout linearLayoutResults;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            quizNumberTextView = itemView.findViewById(R.id.quizNumberTextView);
            topicTextView = itemView.findViewById(R.id.topicTextView);
            markTextView = itemView.findViewById(R.id.markTextView);
            linearLayoutResults = itemView.findViewById(R.id.linearLayoutResults);
        }
    }
}

