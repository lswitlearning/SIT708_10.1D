package com.example.task10_1d;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// RecyclerView Adapter for displaying quiz results
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<QuizQuestion> questions;

    public ResultAdapter(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    // This method is called to create new ViewHolder objects
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);
        return new ViewHolder(view);
    }

    // This method binds data to the ViewHolder at a specific position
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QuizQuestion question = questions.get(position);

        // Set the question number
        holder.questionNumber.setText("Question #" + (position + 1));

        // Set the question text
        holder.questionText.setText("Question: " + question.getQuestion());

        // Create a RadioGroup to display the answer options
        RadioGroup radioGroup = new RadioGroup(holder.itemView.getContext());
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        // Iterate through the answer options
        for (String option : question.getOptions()) {
            RadioButton radioButton = new RadioButton(holder.itemView.getContext());
            radioButton.setText(option);

            // Check if this option is the user's selected answer
            if (question.getUserAnswer() != null && question.getUserAnswer().equals(option)) {
                radioButton.setChecked(true);
            }

            radioGroup.addView(radioButton);
        }

        // Clear any existing views and add the RadioGroup to the ViewHolder
        holder.radioGroupContainer.removeAllViews();
        holder.radioGroupContainer.addView(radioGroup);
    }

    // Returns the total number of items in the RecyclerView
    @Override
    public int getItemCount() {
        return questions.size();
    }

    // ViewHolder class to hold references to the views for a single item
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionNumber;
        TextView questionText;
        ViewGroup radioGroupContainer;

        // Constructor to initialize the ViewHolder with the item view
        public ViewHolder(View itemView) {
            super(itemView);
            questionNumber = itemView.findViewById(R.id.questionNumber);
            questionText = itemView.findViewById(R.id.questionText);
            radioGroupContainer = itemView.findViewById(R.id.radioGroupContainer);
        }
    }
}