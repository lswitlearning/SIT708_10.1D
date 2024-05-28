package com.example.task10_1d;

import java.util.List;

public class HistoryItem {
    private int quizNumber;
    private String topic;
    private String mark;
    private List<String> questions;

    // 构造函数
    public HistoryItem(int quizNumber, String topic, String mark, List<String> questions) {
        this.quizNumber = quizNumber;
        this.topic = topic;
        this.mark = mark;
        this.questions = questions;
    }

    // Getters
    public int getQuizNumber() {
        return quizNumber;
    }

    public String getTopic() {
        return topic;
    }

    public String getMark() {
        return mark;
    }

    public List<String> getQuestions() {
        return questions;
    }
}
