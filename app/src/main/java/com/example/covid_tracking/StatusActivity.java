package com.example.covid_tracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StatusActivity extends AppCompatActivity {
    TextView status,userName,business;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.status);
        String user = getIntent().getStringExtra("USERNAME");
        String buss = getIntent().getStringExtra("ADMIN");
        String stat = getIntent().getStringExtra("STATUS");
        status = findViewById(R.id.status);
        status.setText(stat);
        userName = findViewById(R.id.userNameStatus);
        userName.setText(user);
        business = findViewById(R.id.busNameStatus);
        business.setText(buss);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), UserPageActivity.class);
        String user = getIntent().getStringExtra("USERNAME");
        myIntent.putExtra("USERNAME",user);
        startActivityForResult(myIntent, 0);
        return true;
    }


}
