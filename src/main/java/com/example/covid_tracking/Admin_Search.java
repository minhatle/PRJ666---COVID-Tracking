package com.example.covid_tracking;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

public class Admin_Search extends AppCompatActivity {
    SearchView searchView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_search);

        searchView = findViewById(R.id.search_phone);
    }
}
