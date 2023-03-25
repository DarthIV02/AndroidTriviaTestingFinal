package com.vicentearmenta.androidtriviatesting.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.vicentearmenta.androidtriviatesting.R;
import com.vicentearmenta.androidtriviatesting.database.DatabaseOperations;
import com.vicentearmenta.androidtriviatesting.databinding.ActivityResultBinding;
import com.vicentearmenta.androidtriviatesting.models.Result;

import java.util.List;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;

    DatabaseOperations mDBOperations;

    String userId;

    List<Result> results;

    // Inital declarations for everything table related
    TableLayout tl;
    TableRow tr;
    TextView user;
    TextView score;

    boolean playerAdded = false;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        userId = intent.getStringExtra("USERID");

        mDBOperations = new DatabaseOperations(ResultActivity.this);

        results = mDBOperations.getResults(userId);

        int currentPlayerScore = Integer.parseInt(results.get(0).getScore());

        tl = (TableLayout) binding.tableResults;
        // Rows creation and parameters

        for(int i=1; i < results.size(); i++){

            int scoreValue = Integer.parseInt(results.get(i).getScore());
            String userValue = results.get(i).getUser();

            tr = new TableRow(ResultActivity.this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            user = new TextView(ResultActivity.this);
            user.setGravity(Gravity.CENTER);
            score = new TextView(ResultActivity.this);
            score.setGravity(Gravity.CENTER);

            if ( scoreValue > currentPlayerScore || playerAdded){
                setParametersRow(R.drawable.border2, userValue, scoreValue);
            } else {
                // Add current player to the scoreboard
                setParametersRow(R.drawable.border, results.get(0).getUser(), currentPlayerScore);

                playerAdded = true;
                i--;
            }

            // concatenate textview to table row
            tr.addView(user);
            tr.addView(score);

            // concatenate row to table
            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        if(!playerAdded){
            tr = new TableRow(ResultActivity.this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            user = new TextView(ResultActivity.this);
            user.setGravity(Gravity.CENTER);
            score = new TextView(ResultActivity.this);
            score.setGravity(Gravity.CENTER);

            setParametersRow(R.drawable.border, results.get(0).getUser(), currentPlayerScore);
            // concatenate textview to table row
            tr.addView(user);
            tr.addView(score);

            // concatenate row to table
            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        binding.buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setParametersRow(int border, String userValue, int scoreValue){
        user.setText(userValue);
        score.setText(String.valueOf(scoreValue));

        tr.setBackground(getResources().getDrawable(border));
        user.setBackground(getResources().getDrawable(border));
        user.setTextSize(13);
        score.setTextSize(13);
        user.setHeight(60);
        score.setHeight(60);
        user.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
        score.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
    }
}