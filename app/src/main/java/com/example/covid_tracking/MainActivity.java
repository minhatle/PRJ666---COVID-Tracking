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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {

    EditText login_email, login_pass;
    String email;
    private static final String SERVER = "https://hidden-caverns-06695.herokuapp.com/api/users/login";
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_email = findViewById(R.id.login_email);
        login_pass = findViewById(R.id.login_pass);

        Button btn_login = findViewById(R.id.btn_login);
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

        TextView create_new_acc = findViewById(R.id.create_new_acc);
        create_new_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });
    }

    private void login() throws JSONException, UnsupportedEncodingException {
        email = login_email.getText().toString().trim();
        String pass = login_pass.getText().toString();

        JSONObject jsonParams = new JSONObject();

        boolean value = false;

        if (TextUtils.isEmpty(email)) {
            login_email.setError("Please Enter Email");
            login_email.requestFocus();
            return;
        } else
            jsonParams.put("userName", email);
        value = true;

        if (TextUtils.isEmpty(pass)) {
            login_pass.setError("Please Enter Password");
            login_pass.requestFocus();
            return;
        } else
            jsonParams.put("password", pass);
        value = true;

        if (value) {

            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    Intent intent = new Intent(getBaseContext(), UserPageActivity.class);
                    intent.putExtra("USERNAME", email);
                    startActivity(intent);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    login_pass.setError("Incorrect Password");
                    login_pass.requestFocus();

                }

            };
            StringEntity entity = new StringEntity(jsonParams.toString());
            client.post(MainActivity.this, SERVER, entity, "application/json", responseHandler);

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        super.onOptionsItemSelected(menuItem);
        int id = menuItem.getItemId();
       if  (id == R.id.admin_login){
                Intent intent = new Intent(MainActivity.this, Admin_Login.class);
                //intent.putParcelableArrayListExtra("Admin Login", characters);
                startActivity(intent);}
       else if (id == R.id.admin_signup){
                Intent intent2 = new Intent(MainActivity.this, Admin_SignUpActivity.class);
                //intent.putParcelableArrayListExtra("Admin Login", characters);
                startActivity(intent2);}



        return true;
    }
}