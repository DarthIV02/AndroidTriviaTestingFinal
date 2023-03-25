package com.vicentearmenta.androidtriviatesting.models;

public class Answer {

    private String answerId;

    private String answerText;

    public String getAnswerId(){ return answerId; }

    public String getAnswerText(){ return answerText; }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
