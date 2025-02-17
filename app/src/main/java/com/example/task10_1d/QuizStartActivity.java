package com.example.task10_1d;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizStartActivity extends AppCompatActivity {

    private TextView smallTaskTextView;
    private TextView loadingText;
    private ProgressBar loadingIndicator;
    private Retrofit retrofit;
    private QuizService quizService;
    private List<String> selectedInterests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_start_page);

        // Initialize UI elements
        smallTaskTextView = findViewById(R.id.smallTask);
        Button startButton = findViewById(R.id.startButton);
        Button refreshButton = findViewById(R.id.refreshButton);
        loadingText = findViewById(R.id.loadingText);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        Button historyButton = findViewById(R.id.historyButton);

        Button shareButton = findViewById(R.id.shareButton);

        Button upgradeButton = findViewById(R.id.upgradeButton);

        // Configure Retrofit for API requests
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")  // Base URL for API
                .addConverterFactory(GsonConverterFactory.create())  // Use Gson to convert JSON data
                .client(new OkHttpClient.Builder().readTimeout(60, java.util.concurrent.TimeUnit.SECONDS).build())  // Configure OkHttp client
                .build();

        quizService = retrofit.create(QuizService.class);  // Create service instance for API requests

        // Retrieve the selected interests from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        Set<String> selectedInterestsSet = prefs.getStringSet("selectedInterests", new HashSet<>());
        selectedInterests = new ArrayList<>(selectedInterestsSet);  // Convert to ArrayList

        if (selectedInterests == null || selectedInterests.isEmpty()) {  // If no interests are selected
            smallTaskTextView.setText("No topics selected.");
            return;  // Exit early if there are no selected interests
        }

        randomizeTopic();  // Randomly select and display a topic

        // Set up the "START" button click listener
        startButton.setOnClickListener(view -> {
            // Show loading text and indicator when starting to fetch quiz data
            loadingText.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.VISIBLE);
            fetchQuizDataAndNavigate(getCurrentTopic());  // Fetch quiz data based on the current topic
        });

        // Set up the "REFRESH TOPIC" button click listener
        refreshButton.setOnClickListener(view -> {
            randomizeTopic();  // Refresh the topic randomly
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizStartActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizStartActivity.this, ShareActivity.class);
                startActivity(intent);
            }
        });

        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizStartActivity.this, UpgradeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void randomizeTopic() {
        if (selectedInterests == null || selectedInterests.isEmpty()) {  // If no interests are available
            smallTaskTextView.setText("No topics available.");
            return;
        }

        // Select a random topic from the list of selected interests
        Random random = new Random();
        String selectedTopic = selectedInterests.get(random.nextInt(selectedInterests.size()));
        smallTaskTextView.setText("The topic is " + selectedTopic);  // Display the selected topic
    }

    private void fetchQuizDataAndNavigate(String topic) {
        // Create a call to fetch the quiz data
        Call<QuizResponse> call = quizService.getQuiz(topic);

        // Asynchronous call to fetch data
        call.enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                // Hide the loading text and indicator
                loadingText.setVisibility(View.GONE);
                loadingIndicator.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {  // If the response is successful
                    Gson gson = new Gson();
                    String quizDataJson = gson.toJson(response.body());  // Convert the response body to JSON

                    Intent intent = new Intent(QuizStartActivity.this, QuizPageActivity.class);  // Create an intent to start the QuizPageActivity
                    intent.putExtra("quiz_data", quizDataJson);  // Pass the quiz data to the new activity
                    startActivity(intent);  // Start the new activity
                } else {
                    Toast.makeText(QuizStartActivity.this, "Error fetching quiz.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                // Hide the loading text and indicator, then show a failure message
                loadingText.setVisibility(View.GONE);
                loadingIndicator.setVisibility(View.GONE);
                Toast.makeText(QuizStartActivity.this, "Failed to fetch quiz.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCurrentTopic() {
        return smallTaskTextView.getText().toString().replace("The topic is ", "");  // Get the current topic without the prefix
    }
}