package com.example.task10_1d;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ResultPageActivity extends AppCompatActivity {

    private LinearLayout innerResultLayout;  // Container for displaying the question and answer cards

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

        // Initialize the UI components
        innerResultLayout = findViewById(R.id.innerResultLayout);
        Button finishButton = findViewById(R.id.finishButton);

        finishButton.setOnClickListener(view -> {
            Intent backIntent = new Intent(ResultPageActivity.this, QuizStartActivity.class);
            startActivity(backIntent);
        });

        Intent intent = getIntent();
        ArrayList<QuizQuestion> answeredQuestions =
                intent.getParcelableArrayListExtra("all_questions");

        if (answeredQuestions == null || answeredQuestions.isEmpty()) {
            Toast.makeText(this, "No results available.", Toast.LENGTH_SHORT).show();
            return;
        }


        innerResultLayout.removeAllViews();

        // Loop through the list of questions to create individual cards for each
        for (int i = 0; i < answeredQuestions.size(); i++) {
            QuizQuestion quizQuestion = answeredQuestions.get(i);

            // Create a new LinearLayout to represent the question card
            LinearLayout questionLayout = new LinearLayout(this);
            questionLayout.setOrientation(LinearLayout.VERTICAL);
            questionLayout.setPadding(15, 15, 15, 15);
            questionLayout.setBackgroundResource(R.drawable.edit_text_border);

            // Create layout parameters for the card with a bottom margin
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.bottomMargin = 15;  // Set the bottom margin for the card
            questionLayout.setLayoutParams(layoutParams);

            // Create and set the question number
            TextView questionNumber = new TextView(this);
            questionNumber.setText("Question #" + (i + 1));  // Display the question number
            questionNumber.setTextColor(ContextCompat.getColor(this, R.color.black));  // Set the text color
            questionNumber.setTextSize(16f);  // Set the text size

            // Create and set the question text
            TextView questionText = new TextView(this);
            questionText.setText("Question: " + quizQuestion.getQuestion());
            questionText.setTextColor(ContextCompat.getColor(this, R.color.black));
            questionText.setTextSize(16f);

            // Create and set the user's answer
            TextView userAnswerText = new TextView(this);
            String userAnswer = quizQuestion.getUserAnswer() != null
                    ? quizQuestion.getUserAnswer()
                    : "No answer selected";
            userAnswerText.setText("Your Answer: " + userAnswer);
            userAnswerText.setTextColor(ContextCompat.getColor(this, R.color.black));
            userAnswerText.setTextSize(16f);  // Set the text size

            TextView correctAnswerText = new TextView(this);
            correctAnswerText.setText("Correct Answer: " + quizQuestion.getCorrectAnswer());
            correctAnswerText.setTextColor(ContextCompat.getColor(this, R.color.black));
            correctAnswerText.setTextSize(16f);

            if (quizQuestion.getUserAnswer() != null
                    && quizQuestion.getUserAnswer().equals(quizQuestion.getCorrectAnswer())) {
                correctAnswerText.append(" (You are correct)");
                correctAnswerText.setTextColor(ContextCompat.getColor(this, R.color.green));
            } else {
                correctAnswerText.append(" (You are not correct)");
                correctAnswerText.setTextColor(ContextCompat.getColor(this, R.color.red));
            }

            // Add the components to the question card
            questionLayout.addView(questionNumber);
            questionLayout.addView(questionText);
            questionLayout.addView(userAnswerText);
            questionLayout.addView(correctAnswerText);

            // Add the question card to the inner layout
            innerResultLayout.addView(questionLayout);
        }
    }
}