package com.example.covid_tracking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Admin_Login extends AppCompatActivity {
    EditText id_number, admin_pass;

    @Override
    public void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.admin_login);

        id_number = findViewById(R.id.id_number);
        admin_pass = findViewById(R.id.admin_pass);

        Button btn_login = findViewById(R.id.btn_adminLogin);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){
        String email = id_number.getText().toString().trim();
        String pass = admin_pass.getText().toString();

        boolean value = false;

        if(TextUtils.isEmpty(email)){
            id_number.setError("Please Enter Number");
            id_number.requestFocus();
            return;
        }else
            value = true;

        if(TextUtils.isEmpty(pass)){
            admin_pass.setError("Please Enter Password");
            admin_pass.requestFocus();
            return;
        }else
            value = true;

        if(value){
            startActivity(new Intent(this, Admin_Search.class));
        }
    }
}
