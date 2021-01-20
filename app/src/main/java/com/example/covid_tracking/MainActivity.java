package com.example.covid_tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText login_email, login_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_email = findViewById(R.id.login_email);
        login_pass = findViewById(R.id.login_pass);

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                login();

            }
        });

        TextView create_new_acc = findViewById(R.id.create_new_acc);
        create_new_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });
    }

    private void login(){
        String email = login_email.getText().toString().trim();
        String pass = login_pass.getText().toString();

        boolean value = false;

        if(TextUtils.isEmpty(email)){
            login_email.setError("Please Enter Email");
            login_email.requestFocus();
            return;
        }else
            value = true;

        if(TextUtils.isEmpty(pass)){
            login_pass.setError("Please Enter Password");
            login_pass.requestFocus();
            return;
        }else
            value = true;

        if(value){
            startActivity(new Intent(MainActivity.this, StatusActivity.class));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        super.onOptionsItemSelected(menuItem);
        switch (menuItem.getItemId()){
            case R.id.admin_login:{
                Intent intent = new Intent (MainActivity.this, Admin_Login.class);
                //intent.putParcelableArrayListExtra("Admin Login", characters);
                startActivity(intent);
            }
        }
        return true;
    }
}