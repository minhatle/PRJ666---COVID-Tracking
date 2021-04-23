package com.example.covid_tracking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.client.android.CaptureActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class SelectionActivity extends AppCompatActivity {
    private static final String SERVER = "https://hidden-caverns-06695.herokuapp.com/api/admin/postalCode";
    private static final String SERVER2 = "https://hidden-caverns-06695.herokuapp.com/api/admin/phoneNumber";
    private static final String USERSERVER = "https://hidden-caverns-06695.herokuapp.com/api/users/login";
    private static final String USERSERVER2 = "https://hidden-caverns-06695.herokuapp.com/api/users/addBuss/";
    private static final String ADMINSERVER = "https://hidden-caverns-06695.herokuapp.com/api/admin/getAdmin/";
    private static final int PERMISSION_REQUEST_CAMERA = 1;
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.bussiness_selection);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Toast.makeText(this,"camera permission granted",Toast.LENGTH_LONG).show();


        } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }
        Button btn_qr = findViewById(R.id.btn_qr);
        btn_qr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
                    startActivityForResult(intent, 0);

                } catch (Exception e) {

                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                    startActivity(marketIntent);

                }
            }
        });


        Button btn_postal = findViewById(R.id.btn_postal);
        btn_postal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    postalSearch();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btn_phone = findViewById(R.id.btn_phone);
        btn_phone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    phoneSearch();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                JSONObject adminJson = new JSONObject();
                try {
                    adminJson = new JSONObject(contents);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String admin = "";
                try {
                    admin = adminJson.getString("userName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String finalAdmin = admin;
                JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                        Intent intent = new Intent(getBaseContext(), StatusActivity.class);
                        try {
                            JSONObject questions = json.getJSONObject("questionnaire");
                            Boolean sym = questions.getBoolean("symptoms");
                            Boolean trav = questions.getBoolean("travel");
                            Boolean cont = questions.getBoolean("contact");
                            if (sym || trav || cont){
                                intent.putExtra("STATUS", "SCREENING UNSUCCESSFUL");
                            }
                            else {
                                intent.putExtra("STATUS", "SCREENING SUCCESSFUL");
                                addUserToAdmin(finalAdmin);
                            }
                        } catch (JSONException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                            intent.putExtra("STATUS", "SCREENING UNSUCCESSFUL");
                        }
                        String username = getIntent().getStringExtra("USERNAME");
                        intent.putExtra("USERNAME", username);
                        intent.putExtra("ADMIN", finalAdmin);

                        startActivity(intent);

                    }

                };
                StringEntity entity = null;
                try {
                    entity = new StringEntity(adminJson.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                client.post(SelectionActivity.this, USERSERVER, entity, "application/json", responseHandler);
            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), UserPageActivity.class);
        String user = getIntent().getStringExtra("USERNAME");
        myIntent.putExtra("USERNAME",user);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
            Toast.makeText(this, "Camera access is required to Scan The Barcode.",
                    Toast.LENGTH_LONG).show();


            // Request the permission
            ActivityCompat.requestPermissions(SelectionActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);



        } else {
            Toast.makeText(this,
                    "<b>Camera could not be opened.</b>\\nThis occurs when the camera is not available (for example it is already in use) or if the system has denied access (for example when camera access has been disabled).", Toast.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    private void addUserToAdmin(String adminUsername) throws JSONException, UnsupportedEncodingException {
        JSONObject jsonParams = new JSONObject();
        JSONObject jsonParams2 = new JSONObject();
        JSONObject jsonParamsOutter = new JSONObject();
        JSONObject jsonParamsOutter2 = new JSONObject();
        String username = getIntent().getStringExtra("USERNAME");
        jsonParams.put("userName",username);
        jsonParams2.put("userName",adminUsername);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        jsonParams.put("date", date);
        jsonParams2.put("date", date);
        jsonParamsOutter.put("users",jsonParams);
        jsonParamsOutter2.put("businesses",jsonParams2);
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler();
        StringEntity entity = new StringEntity(jsonParamsOutter.toString());
        StringEntity entity2 = new StringEntity(jsonParamsOutter2.toString());
        client.put(SelectionActivity.this, ADMINSERVER+ adminUsername, entity, "application/json",responseHandler);
        client.put(SelectionActivity.this, USERSERVER2+ username, entity2, "application/json",responseHandler);

    }
    private void postalSearch() throws JSONException, UnsupportedEncodingException {
        EditText postalCode = findViewById(R.id.search_postalCode);
        String postal = postalCode.getText().toString().trim();
        JSONObject jsonParams = new JSONObject();

        boolean value = false;

        if (TextUtils.isEmpty(postal)) {
            postalCode.setError("Please Enter Postal Code");
            postalCode.requestFocus();
            return;
        } else
            jsonParams.put("postalCode", postal);
        value = true;


        if (value) {

            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    JSONObject jsonParams = new JSONObject();
                    String admin = "";
                    try {
                         admin = json.getString("userName");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String username = getIntent().getStringExtra("USERNAME");
                    try {
                        jsonParams.put("userName", username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final String finalAdmin = admin;
                    JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                            Intent intent = new Intent(getBaseContext(), StatusActivity.class);
                            try {
                                JSONObject questions = json.getJSONObject("questionnaire");
                                Boolean sym = questions.getBoolean("symptoms");
                                Boolean trav = questions.getBoolean("travel");
                                Boolean cont = questions.getBoolean("contact");
                                if (sym || trav || cont){
                                    intent.putExtra("STATUS", "SCREENING UNSUCCESSFUL");
                                }
                                else {
                                    intent.putExtra("STATUS", "SCREENING SUCCESSFUL");
                                    addUserToAdmin(finalAdmin);
                                }
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                intent.putExtra("STATUS", "SCREENING UNSUCCESSFUL");
                            }
                            String username = getIntent().getStringExtra("USERNAME");
                            intent.putExtra("USERNAME", username);
                            intent.putExtra("ADMIN", finalAdmin);

                            startActivity(intent);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            EditText errorPostal = findViewById(R.id.search_postalCode);
                            errorPostal.setError("UserName Not Found");
                            errorPostal.requestFocus();

                        }


                    };
                    StringEntity entity = null;
                    try {
                        entity = new StringEntity(jsonParams.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    client.post(SelectionActivity.this, USERSERVER, entity, "application/json", responseHandler);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    EditText errorPostal = findViewById(R.id.search_postalCode);
                    errorPostal.setError("Postal Code Not Found");
                    errorPostal.requestFocus();

                }

            };
            StringEntity entity = new StringEntity(jsonParams.toString());
            client.post(SelectionActivity.this, SERVER, entity, "application/json", responseHandler);

        }
    }
    private void phoneSearch() throws JSONException, UnsupportedEncodingException {
        EditText phoneNumber = findViewById(R.id.search_phone);
        String phone = phoneNumber.getText().toString().trim();
        JSONObject jsonParams = new JSONObject();

        boolean value = false;

        if (TextUtils.isEmpty(phone)){
            phoneNumber.setError("Please Enter Postal Code");
            phoneNumber.requestFocus();
            return;
        } else
            jsonParams.put("phoneNumber", phone);
        value = true;


        if (value) {

            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    JSONObject jsonParams = new JSONObject();
                    String admin = "";
                    try {
                        admin = json.getString("userName");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String username = getIntent().getStringExtra("USERNAME");
                    try {
                        jsonParams.put("userName", username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final String finalAdmin = admin;
                    JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                            Intent intent = new Intent(getBaseContext(), StatusActivity.class);
                            try {
                                JSONObject questions = json.getJSONObject("questionnaire");
                                Boolean sym = questions.getBoolean("symptoms");
                                Boolean trav = questions.getBoolean("travel");
                                Boolean cont = questions.getBoolean("contact");
                                if (sym || trav || cont){
                                    intent.putExtra("STATUS", "SCREENING UNSUCCESSFUL");
                                }
                                else {
                                    intent.putExtra("STATUS", "SCREENING SUCCESSFUL");
                                    addUserToAdmin(finalAdmin);
                                }
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                intent.putExtra("STATUS", "SCREENING UNSUCCESSFUL");
                            }
                            String username = getIntent().getStringExtra("USERNAME");
                            intent.putExtra("USERNAME", username);
                            intent.putExtra("ADMIN", finalAdmin);

                            startActivity(intent);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            EditText errorPostal = findViewById(R.id.search_phone);
                            errorPostal.setError("UserName Not Found");
                            errorPostal.requestFocus();

                        }


                    };
                    StringEntity entity = null;
                    try {
                        entity = new StringEntity(jsonParams.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    client.post(SelectionActivity.this, USERSERVER, entity, "application/json", responseHandler);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    EditText errorPostal = findViewById(R.id.search_phone);
                    errorPostal.setError("Phone Number Not Found");
                    errorPostal.requestFocus();

                }

            };
            StringEntity entity = new StringEntity(jsonParams.toString());
            client.post(SelectionActivity.this, SERVER2, entity, "application/json", responseHandler);

        }
    }
}
