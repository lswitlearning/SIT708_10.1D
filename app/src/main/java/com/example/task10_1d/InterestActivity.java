package com.example.task10_1d;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class InterestActivity extends AppCompatActivity {

    private List<String> selectedInterests = new ArrayList<>();
    private static final int MAX_INTERESTS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_page);

        setInterestClickListener(R.id.button, "Maths");
        setInterestClickListener(R.id.button1, "Solar Planet");
        setInterestClickListener(R.id.button2, "City of Country");
        setInterestClickListener(R.id.button3, "History");
        setInterestClickListener(R.id.button4, "Technology");
        setInterestClickListener(R.id.button5, "Art and Culture");
        setInterestClickListener(R.id.button6, "Movies");
        setInterestClickListener(R.id.button7, "Sports");
        setInterestClickListener(R.id.button8, "Geography");

        Button doneButton = findViewById(R.id.registerButton);
        doneButton.setOnClickListener(view -> {
            if (selectedInterests.size() > MAX_INTERESTS) {
                Toast.makeText(this, "You can select up to 3 topics.", Toast.LENGTH_SHORT).show();
                return;
            }

            saveInterests();
            finish();
        });
    }

    private void setInterestClickListener(int buttonId, String interest) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(view -> toggleInterest(button, interest));
    }

    private void toggleInterest(Button button, String interest) {
        if (selectedInterests.contains(interest)) {
            selectedInterests.remove(interest);
            button.setBackgroundResource(R.drawable.buttonbg);
        } else {
            if (selectedInterests.size() >= MAX_INTERESTS) {
                Toast.makeText(this, "You can select up to 3 topics.", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedInterests.add(interest);
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.pressBtn_color));
        }
    }
    private void saveInterests() {
        SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("selectedInterests", new HashSet<>(selectedInterests));  // 保存兴趣集合
        editor.apply();
    }
}