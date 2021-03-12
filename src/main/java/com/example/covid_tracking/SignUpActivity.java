package com.example.covid_tracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

public class SignUpActivity extends AppCompatActivity {
    EditText signup_name, signup_email, signup_pass;
    private static final String SERVER = "https://hidden-caverns-06695.herokuapp.com/api/users/";
    private static AsyncHttpClient client = new AsyncHttpClient();

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
                try {
                    createNewAccount();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void createNewAccount() throws JSONException, UnsupportedEncodingException {
        boolean value = false;
        String name = signup_name.getText().toString().trim();
        String email = signup_email.getText().toString().trim();
        String pass = signup_pass.getText().toString().trim();
        JSONObject jsonParams = new JSONObject();


        if(TextUtils.isEmpty(name)){
            signup_name.setError("Please Enter Name");
            signup_name.requestFocus();
            return;
        }
        else
            jsonParams.put("userName", name);
            value = true;

        if(TextUtils.isEmpty(email)){
            signup_email.setError("Please Enter Email");
            signup_email.requestFocus();
            return;
        }
        else
            jsonParams.put("email", email);
            value = true;

        if(TextUtils.isEmpty(pass)){
            signup_pass.setError("Please Enter Password");
            signup_pass.requestFocus();
            return;
        }else
            jsonParams.put("password", pass);
            value = true;

        if (value){
            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler();
            StringEntity entity = new StringEntity(jsonParams.toString());
            client.post(SignUpActivity.this, SERVER, entity, "application/json",responseHandler);
            final ProgressDialog pd = new ProgressDialog(SignUpActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            String url = "";

            Intent intent = new Intent(getBaseContext(), QuestionActivity.class);
            intent.putExtra("USERNAME",name);
            startActivity(intent);
        }
    }

}
