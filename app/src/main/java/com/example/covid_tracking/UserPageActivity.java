package com.example.covid_tracking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class UserPageActivity extends AppCompatActivity {
    private static final String SERVER = "https://hidden-caverns-06695.herokuapp.com/api/users/login";
    private static AsyncHttpClient client = new AsyncHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.user_page);
        String username = getIntent().getStringExtra("USERNAME");


        try {
            getUser(username);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Button btn_status = findViewById(R.id.btn_status);
        btn_status.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddImage.class);
                String username = getIntent().getStringExtra("USERNAME");
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        Button btn_question = findViewById(R.id.btn_question);
        btn_question.setVisibility(View.GONE);
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
        btn_screen.setVisibility(View.GONE);
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

    private void getUser(String username) throws JSONException, UnsupportedEncodingException {

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("userName", username);


            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    try {
                        TextView welcome = findViewById(R.id.welcome);
                        String name = json.getString("firstName");
                        String welcomeText = "Welcome " + name;
                        JSONObject questions = json.getJSONObject("questionnaire");
                        String lastDateStr = questions.getString("date");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date lastDate = sdf.parse(lastDateStr);
                        Date currDate = new Date();
                        long diff = currDate.getTime() - lastDate.getTime();
                        long seconds = diff / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        if (hours > 36){
                            welcomeText = welcomeText + ", Your questionnaire results have expired!";
                            Button btn_question = findViewById(R.id.btn_question);
                            btn_question.setVisibility(View.VISIBLE);
                        }
                        else{
                            welcomeText = welcomeText + ", Your questionnaire results are valid for " + String.valueOf(36 - hours) + " hours";
                            Button btn_question = findViewById(R.id.btn_question);
                            btn_question.setVisibility(View.VISIBLE);
                            Button btn_screen = findViewById(R.id.btn_screen);
                            btn_screen.setVisibility(View.VISIBLE);

                        }
                        welcome.setText(welcomeText);



                    } catch (JSONException e) {
                        e.printStackTrace();
                        TextView welcome = findViewById(R.id.welcome);
                        String name = null;
                        try {
                            name = json.getString("firstName");
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                        String welcomeText = "Welcome " + name + ", You have not yet filled out the screening questionnaire!";
                        welcome.setText(welcomeText);
                        Button btn_question = findViewById(R.id.btn_question);
                        btn_question.setVisibility(View.VISIBLE);
                    }
                    catch(ParseException e){
                        TextView welcome = findViewById(R.id.welcome);
                        String name = null;
                        try {
                            name = json.getString("firstName");
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                        String welcomeText = "Welcome " + name + ", You have not yet filled out the screening questionnaire!";
                        welcome.setText(welcomeText);
                        Button btn_question = findViewById(R.id.btn_question);
                        btn_question.setVisibility(View.VISIBLE);
                    }
                }

            };
            StringEntity entity = new StringEntity(jsonParams.toString());
            client.post(UserPageActivity.this, SERVER, entity, "application/json", responseHandler);

        }
    }

