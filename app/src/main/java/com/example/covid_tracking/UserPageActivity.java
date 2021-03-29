package com.example.covid_tracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class UserPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.user_page);

        Button btn_question = findViewById(R.id.btn_question);
        btn_question.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), QuestionActivity.class);
                String username = getIntent().getStringExtra("USERNAME");
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        Button btn_screen = findViewById(R.id.btn_screen);
        btn_screen.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPageActivity.this, SelectionActivity.class);
                String username = getIntent().getStringExtra("USERNAME");
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
