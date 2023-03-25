package com.vicentearmenta.androidtriviatesting.models;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String questionId;

    private String questionText;

    private String correctAnswer;
    private Answer optionA;
    private Answer optionB;
    private Answer optionC;
    private Answer optionD;

    public Question(String id, String text, String answer, Answer optionA, Answer optionB, Answer optionC, Answer optionD) {
        this.questionId = id;
        this.questionText = text;
        this.correctAnswer = answer;

        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Answer getOptionA() {
        return optionA;
    }

    public void setOptionA(Answer optionA) {
        this.optionA = optionA;
    }

    public Answer getOptionB() {
        return optionB;
    }

    public void setOptionB(Answer optionB) {
        this.optionB = optionB;
    }

    public Answer getOptionC() {
        return optionC;
    }

    public void setOptionC(Answer optionC) {
        this.optionC = optionC;
    }

    public Answer getOptionD() {
        return optionD;
    }

    public void setOptionD(Answer optionD) {
        this.optionD = optionD;
    }

    public List<Answer> getAllAnswers(){
        List<Answer> answers = new ArrayList<>();
        answers.add(this.getOptionA());
        answers.add(this.getOptionB());
        answers.add(this.getOptionC());
        answers.add(this.getOptionD());

        return answers;
    }
}
