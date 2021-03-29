package com.example.covid_tracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

public class QuestionActivity extends AppCompatActivity {
    CheckBox symptomYes, symptomNo, contactYes, contactNo,travelYes,travelNo;
    String username;
    private final String SERVER2 = "https://hidden-caverns-06695.herokuapp.com/api/users/getUser/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username = getIntent().getStringExtra("USERNAME");
        symptomYes = findViewById(R.id.symptomsCheckYes);
        symptomNo = findViewById(R.id.symptomsCheckNo);
        contactYes = findViewById(R.id.contactCheckYes);
        contactNo = findViewById(R.id.contactCheckNo);
        travelYes = findViewById(R.id.travelCheckYes);
        travelNo = findViewById(R.id.travelCheckNo);

        Button btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    updateAccount();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), UserPageActivity.class);
        String user = getIntent().getStringExtra("USERNAME");
        myIntent.putExtra("USERNAME",user);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private void updateAccount() throws JSONException, UnsupportedEncodingException {
        boolean value = false;
        Boolean symYes = symptomYes.isChecked();
        Boolean symNo = symptomNo.isChecked();
        Boolean contYes = contactYes.isChecked();
        Boolean contNo = contactNo.isChecked();
        Boolean travYes = travelYes.isChecked();
        Boolean travNo = travelNo.isChecked();
        JSONObject jsonParamsOutter = new JSONObject();
        JSONObject jsonParamsInner = new JSONObject();



        if(!symYes && !symNo){
            symptomNo.setError("Please Select Yes or No");
            symptomNo.requestFocus();
            return;
        }
        else {
            if (symNo) {
                jsonParamsInner.put("symptoms", false);
            } else {
                jsonParamsInner.put("symptoms", true);
            }
            value = true;
        }

        if(!contYes && !contNo){
            contactNo.setError("Please Select Yes or No");
            contactNo.requestFocus();
            return;
        }
        else {
            if (contNo) {
                jsonParamsInner.put("contact", false);
            } else {
                jsonParamsInner.put("contact", true);
            }
            value = true;
        }

        if(!travYes && !travNo){
            travelNo.setError("Please Select Yes or No");
            travelNo.requestFocus();
            return;
        }
        else {
            if (travNo) {
                jsonParamsInner.put("travel", false);
            } else {
                jsonParamsInner.put("travel", true);
            }
            value = true;
        }

        if (value){
            jsonParamsOutter.put("questionnaire",jsonParamsInner);
            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler();
            StringEntity entity = new StringEntity(jsonParamsOutter.toString());
            client.put(QuestionActivity.this, SERVER2 + username, entity, "application/json",responseHandler);
            final ProgressDialog pd = new ProgressDialog(QuestionActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            String url = "";

            Intent intent = new Intent(getBaseContext(), UserPageActivity.class);
            String username = getIntent().getStringExtra("USERNAME");
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        }
    }

}

