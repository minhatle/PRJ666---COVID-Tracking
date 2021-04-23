package com.example.covid_tracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.entity.StringEntity;

public class SignUpActivity extends AppCompatActivity {
    EditText signup_firstName, signup_lastName, signup_user, signup_address, signup_city,
            signup_prov, signup_post, signup_email, signup_pass;


    private static final String SERVER = "https://hidden-caverns-06695.herokuapp.com/api/users/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        signup_firstName = findViewById(R.id.signup_firstName);
        signup_lastName = findViewById(R.id.signup_lastName);
        signup_user = findViewById(R.id.signup_user);
        signup_address = findViewById(R.id.signup_address);
        signup_city = findViewById(R.id.signup_city);
        signup_post = findViewById(R.id.signup_postalCode);
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

        String user = signup_user.getText().toString().trim();
        String email = signup_email.getText().toString().trim();
        String pass = signup_pass.getText().toString().trim();
        String firstName = signup_firstName.getText().toString().trim();
        String lastName = signup_lastName.getText().toString().trim();
        String address = signup_address.getText().toString();
        String city = signup_city.getText().toString().trim();
        String postalCode = signup_post.getText().toString().trim();
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

        if (TextUtils.isEmpty(firstName)) {
            signup_firstName.setError("Please Enter Name");
            signup_firstName.requestFocus();
            return;
        } else
            jsonParams.put("firstName", firstName);

        if (TextUtils.isEmpty(lastName)) {
            signup_lastName.setError("Please Enter Name");
            signup_lastName.requestFocus();
            return;
        } else
            jsonParams.put("lastName", firstName);
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

        String regex = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(postalCode);

        if (TextUtils.isEmpty(postalCode)) {
            signup_post.setError("Please Enter a Postal Code");
            signup_post.requestFocus();
            return;
        }
        else if(!matcher.matches()){
            signup_post.setError("Please Enter a Valid Postal Code");
            signup_post.requestFocus();
            return;
        }
        else
            jsonParams.put("postalCode", postalCode);
        value = true;


        if (value) {
            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler();
            StringEntity entity = new StringEntity(jsonParams.toString());
            client.post(SignUpActivity.this, SERVER, entity, "application/json", responseHandler);
            final ProgressDialog pd = new ProgressDialog(SignUpActivity.this);
            pd.setMessage("Loading...");
            pd.show();


            Intent intent = new Intent(getBaseContext(), UserPageActivity.class);
            intent.putExtra("USERNAME", user);
            startActivity(intent);
        }
    }

}
