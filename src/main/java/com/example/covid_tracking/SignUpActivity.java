package com.example.covid_tracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    EditText signup_name, signup_email, signup_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        signup_name = findViewById(R.id.signup_name);
        signup_email = findViewById(R.id.signup_email);
        signup_pass = findViewById(R.id.signup_pass);


        Button btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();

            }
        });
    }

    private void createNewAccount(){
        boolean value = false;
        String name = signup_name.getText().toString().trim();
        String email = signup_email.getText().toString().trim();
        String pass = signup_pass.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            signup_name.setError("Please Enter Name");
            signup_name.requestFocus();
            return;
        }
        else
            value = true;

        if(TextUtils.isEmpty(email)){
            signup_email.setError("Please Enter Email");
            signup_email.requestFocus();
            return;
        }
        else
            value = true;

        if(TextUtils.isEmpty(pass)){
            signup_pass.setError("Please Enter Password");
            signup_pass.requestFocus();
            return;
        }else
            value = true;

        if (value){
            final ProgressDialog pd = new ProgressDialog(SignUpActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            String url = "";

            Intent intent = new Intent(SignUpActivity.this, StatusActivity.class);
            startActivity(intent);
        }
    }

}
