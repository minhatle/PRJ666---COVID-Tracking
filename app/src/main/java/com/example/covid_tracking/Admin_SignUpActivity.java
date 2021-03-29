package com.example.covid_tracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Admin_SignUpActivity extends AppCompatActivity {
    EditText signup_managerName,signup_ID, signup_user, signup_address, signup_city,
            signup_manage, signup_post, signup_email, signup_pass,signup_number;


    private static final String SERVER = "https://hidden-caverns-06695.herokuapp.com/api/admin/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signup_managerName = findViewById(R.id.signup_manager);
        signup_ID = findViewById(R.id.signup_bussID);
        signup_user = findViewById(R.id.signup_user);
        signup_address = findViewById(R.id.signup_address);
        signup_city = findViewById(R.id.signup_city);
        signup_post = findViewById(R.id.signup_postalCode);
        signup_email = findViewById(R.id.signup_email);
        signup_pass = findViewById(R.id.signup_pass);
        signup_number = findViewById(R.id.signup_number);


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

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private void createNewAccount() throws JSONException, UnsupportedEncodingException {
        boolean value = false;

        String user = signup_user.getText().toString().trim();
        String email = signup_email.getText().toString().trim();
        String pass = signup_pass.getText().toString().trim();
        String managerName = signup_managerName.getText().toString();
        String ID = signup_ID.getText().toString().trim();
        String address = signup_address.getText().toString();
        String city = signup_city.getText().toString().trim();
        String postalCode = signup_post.getText().toString().trim();
        String number = signup_number.getText().toString().trim();
        JSONObject jsonParams = new JSONObject();


        if (TextUtils.isEmpty(user)) {
            signup_user.setError("Please Enter Name");
            signup_user.requestFocus();
            return;
        } else
            jsonParams.put("userName", user);
        value = true;

        if (TextUtils.isEmpty(email)) {
            signup_email.setError("Please Enter Email");
            signup_email.requestFocus();
            return;
        } else
            jsonParams.put("email", email);
        value = true;

        if (TextUtils.isEmpty(pass)) {
            signup_pass.setError("Please Enter Password");
            signup_pass.requestFocus();
            return;
        } else
            jsonParams.put("password", pass);
        value = true;

        if (TextUtils.isEmpty(managerName)) {
            signup_manage.setError("Please Enter a Manager");
            signup_manage.requestFocus();
            return;
        } else
            jsonParams.put("managerName", managerName);

        if (TextUtils.isEmpty(ID)) {
            signup_ID.setError("Please Enter an ID");
            signup_ID.requestFocus();
            return;
        } else
            jsonParams.put("businessID", ID);
        value = true;

        if (TextUtils.isEmpty(address)) {
            signup_address.setError("Please Enter an Address");
            signup_address.requestFocus();
            return;
        } else
            jsonParams.put("address", address);
        value = true;

        if (TextUtils.isEmpty(city)) {
            signup_city.setError("Please Enter a City");
            signup_city.requestFocus();
            return;
        } else
            jsonParams.put("city", city);
        value = true;

        if (TextUtils.isEmpty(postalCode)) {
            signup_post.setError("Please Enter a Postal Code");
            signup_post.requestFocus();
            return;
        } else
            jsonParams.put("postalCode", postalCode);
        value = true;

        if (TextUtils.isEmpty(number)) {
            signup_number.setError("Please Enter a Phone Number");
            signup_number.requestFocus();
            return;
        } else
            jsonParams.put("phoneNumber", number);
        value = true;


        if (value) {
            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    Intent intent = new Intent(getBaseContext(), Admin_Qrcode.class);
                    intent.putExtra("ADMINDATA",json.toString());
                    startActivity(intent);

                }
            };
            StringEntity entity = new StringEntity(jsonParams.toString());
            client.post(Admin_SignUpActivity.this, SERVER, entity, "application/json", responseHandler);
            final ProgressDialog pd = new ProgressDialog(Admin_SignUpActivity.this);
            pd.setMessage("Loading...");
            pd.show();

        }
    }

}
