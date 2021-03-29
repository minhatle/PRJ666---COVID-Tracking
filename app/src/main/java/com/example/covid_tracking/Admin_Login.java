package com.example.covid_tracking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Admin_Login extends AppCompatActivity {
    EditText user, admin_pass;
    private static final String SERVER = "https://hidden-caverns-06695.herokuapp.com/api/admin/login";
//    private static final String TESTSERVER = "http://localhost:8082/api/admin/login";
    private static AsyncHttpClient client = new AsyncHttpClient();
    @Override
    public void onCreate(Bundle savedInstancesState) {

        super.onCreate(savedInstancesState);
        setContentView(R.layout.admin_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = findViewById(R.id.admin_username);
        admin_pass = findViewById(R.id.admin_pass);


        Button btn_login = findViewById(R.id.btn_adminLogin);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private void login() throws JSONException, UnsupportedEncodingException {
        String adminUser = user.getText().toString().trim();
        String pass = admin_pass.getText().toString();

        JSONObject jsonParams = new JSONObject();

        boolean value = false;

        if (TextUtils.isEmpty(adminUser)) {
            user.setError("Please Enter Username");
            user.requestFocus();
            return;
        } else
            jsonParams.put("userName", adminUser);
        value = true;

        if (TextUtils.isEmpty(pass)) {
            admin_pass.setError("Please Enter Password");
            admin_pass.requestFocus();
            return;
        } else
            jsonParams.put("password", pass);
        value = true;

        if (value) {

            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    Intent intent = new Intent(getBaseContext(), Admin_Qrcode.class);
                    intent.putExtra("USERNAME", user.getText().toString().trim());
                    intent.putExtra("ADMINDATA",json.toString());
                    startActivity(intent);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    admin_pass.setError("Incorrect Password");
                    admin_pass.requestFocus();

                }

            };
            StringEntity entity = new StringEntity(jsonParams.toString());
            client.post(Admin_Login.this, SERVER, entity, "application/json", responseHandler);

        }
    }
}
