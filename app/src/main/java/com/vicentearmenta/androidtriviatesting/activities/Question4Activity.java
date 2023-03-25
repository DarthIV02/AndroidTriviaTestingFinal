package com.vicentearmenta.androidtriviatesting.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vicentearmenta.androidtriviatesting.R;
import com.vicentearmenta.androidtriviatesting.database.DatabaseOperations;
import com.vicentearmenta.androidtriviatesting.databinding.ActivityQuestion4Binding;
import com.vicentearmenta.androidtriviatesting.models.Answer;
import com.vicentearmenta.androidtriviatesting.models.Question;

import java.util.List;

public class Question4Activity extends AppCompatActivity {

    ActivityQuestion4Binding binding;

    DatabaseOperations mDBOperations;

    String userId; // Va a estar pasando entre actividades
    String questionsAlreadyAsked; // Va a estar pasando entre actividades

    int finalCorrectAnswerRdBtn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestion4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        userId = intent.getStringExtra("USERID");
        questionsAlreadyAsked = intent.getStringExtra("QUESTIONS");

        mDBOperations = new DatabaseOperations(Question4Activity.this);

        binding.nextButton.setEnabled(false);

        Question question = mDBOperations.getNextQuestion(questionsAlreadyAsked); // Traer lógica
        // de la BD
        // Modificar el layout con el anterior
        binding.questionText.setText(question.getQuestionText());

        String drawableName = "image" + question.getQuestionId();
        binding.imagePlaceholder.setImageResource(getResources().getIdentifier(drawableName,
                "drawable", getPackageName()));

        questionsAlreadyAsked = questionsAlreadyAsked + "," + question.getQuestionId();

        List<Answer> answers = question.getAllAnswers();

        for(int i = 0; i < 4; i++){
            RadioButton tempRadioButton = (RadioButton) binding.rgAnswers.getChildAt(i);
            Answer tempAnswer = answers.get(i); // Bring back the answer object

            if(question.getCorrectAnswer().equals(tempAnswer.getAnswerId())){
                finalCorrectAnswerRdBtn = tempRadioButton.getId(); // Asignar el int correcto del findView
            }

            tempRadioButton.setText(tempAnswer.getAnswerText()); // Set text of the option
        }

        binding.rgAnswers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                binding.nextButton.setEnabled(true); // Button of next
                if (evaluateAnswerSelection(group, checkedId) == 1){
                    mDBOperations.updateScore2(userId);
                };
            }
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Question4Activity.this, Question5Activity.class);
                intent.putExtra("USERID", userId);
                intent.putExtra("QUESTIONS", questionsAlreadyAsked);
                startActivity(intent);
            }
        });
    }

    public int evaluateAnswerSelection(RadioGroup radioGroup, int selectedAnswer){
        int score = 0;

        // Colorear el que esta bien
        RadioButton tempRdButton = findViewById(finalCorrectAnswerRdBtn);
        tempRdButton.setButtonDrawable(R.drawable.ic_correct); // Ic dentro del circulito
        tempRdButton.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.correct))); // Cambiar el color

        if(selectedAnswer == finalCorrectAnswerRdBtn){
            // No hago nada mas que aumentar el score
            score++;
        } else {
            // Seleccionar el que el eligió como wrong
            RadioButton tempRdButton2 = findViewById(selectedAnswer);
            tempRdButton2.setButtonDrawable(R.drawable.ic_wrong);
            tempRdButton2.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.wrong)));
        }

        for(int i =0; i<radioGroup.getChildCount(); i++){
            radioGroup.getChildAt(i).setClickable(false);
        }

        return score;
    }
}