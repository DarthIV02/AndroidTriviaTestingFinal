package com.vicentearmenta.androidtriviatesting.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vicentearmenta.androidtriviatesting.R;
import com.vicentearmenta.androidtriviatesting.database.DatabaseOperations;
import com.vicentearmenta.androidtriviatesting.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DatabaseOperations mDBOperations;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseOperations db = new DatabaseOperations(this);

        mDBOperations = new DatabaseOperations(MainActivity.this);
        //mDBOperations.deleteResults(); // DELETE ALL RESULTS FROM TABLE

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(binding.editName.getText())){ // Useful to compare if its empty string
                    Toast.makeText(MainActivity.this, R.string.toastEmptyName, Toast.LENGTH_LONG).show();
                }
                else{
                    userId = mDBOperations.insertUsername(binding.editName.getText().toString());

                    Intent intent = new Intent(MainActivity.this, Question1Activity.class);
                    intent.putExtra("USERID", userId);
                    intent.putExtra("QUESTIONS", "0");
                    startActivity(intent);
                }
            }
        });
    }
}