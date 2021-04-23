package com.example.covid_tracking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import pub.devrel.easypermissions.EasyPermissions;

public class AddImage extends AppCompatActivity  implements EasyPermissions.PermissionCallbacks {
    private static final String USERSERVER = "https://hidden-caverns-06695.herokuapp.com/api/users/getUser/";
    private static final String ADMINSERVER = "https://hidden-caverns-06695.herokuapp.com/api/admin/getAdmin/";
    private static AsyncHttpClient client = new AsyncHttpClient();
    public static final int PICKFILE_RESULT_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1;
    private Uri fileUri;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.add_test);
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this, getString(R.string.permission), EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                TextView status = findViewById(R.id.currStatus);
                try {
                    String statusText = json.getString("covidStatus");
                    status.setText(statusText);
                    if(statusText == "Positive"){
                        status.setTextColor(getResources().getColor(R.color.Green));
                    }
                    else if (statusText == "Negative"){
                        status.setTextColor(getResources().getColor(R.color.Red));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    status.setText("Unknown");
                    status.setTextColor(getResources().getColor(R.color.Orange));

                }

            }

        };

        client.get(AddImage.this, USERSERVER+  getIntent().getStringExtra("USERNAME"),responseHandler);
        final Spinner dropdown = findViewById(R.id.statusDropdown);

        String[] items = new String[]{"Positive", "Negative"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = dropdown.getSelectedItem().toString();
                JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler();
                JSONObject jsonParams = new JSONObject();

                if (selected == "Positive"){
                    JsonHttpResponseHandler responseHandlerUser = new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                            try {
                                JSONArray businesses = json.getJSONArray("businesses");
                                int len = businesses.length();
                                for(int j=0; j<len; j++) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    JSONObject buss = businesses.getJSONObject(j);
                                    Date tempDate = sdf.parse(buss.getString("date"));
                                    String admin = buss.getString("userName");
                                    String email = buss.getString("email");
                                    trackUsers(admin,tempDate,email);

                                }

                            } catch (JSONException | ParseException | UnsupportedEncodingException e) {
                                e.printStackTrace();

                            }

                        }

                    };

                    client.get(AddImage.this, USERSERVER+  getIntent().getStringExtra("USERNAME"),responseHandlerUser);
                    try {
                        jsonParams.put("covidStatus",selected);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (selected == "Negative"){
                try {
                    jsonParams.put("covidStatus",selected);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }}
                else {
                    try {
                        jsonParams.put("covidStatus","Unknown");
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                    }
                
                StringEntity entity = null;
                try {
                    entity = new StringEntity(jsonParams.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                client.put(AddImage.this, USERSERVER+  getIntent().getStringExtra("USERNAME"), entity, "application/json",responseHandler);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Button btn_add = findViewById(R.id.btn_addImage);
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    ImageView image = findViewById(R.id.statImage);
                    fileUri = data.getData();
                    filePath = fileUri.getPath();
                    File file = new File(fileUri.getPath());
                    image.setImageURI(fileUri);

                }

                break;
        }
    }

    private void trackUsers(String adminUsername, final Date posDate, final String email) throws JSONException, UnsupportedEncodingException {

        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                try {
                    File path = AddImage.this.getFilesDir();
                    File file = new File(path, "tracing.txt");
                    FileOutputStream outputStreamWriter = new FileOutputStream(file);
                    JSONArray userArray = json.getJSONArray("users");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject user = userArray.getJSONObject(i);
                        Date tempDate = sdf.parse(user.getString("date"));
                        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                        if (fmt.format(posDate).equals(fmt.format(tempDate))){
                            String userInfo = "Name: " + user.getString("firstName") + " " +
                                    user.getString("lastName") + "     " + "Email: " + " " +
                                    user.getString("email");
                            outputStreamWriter.write(userInfo.getBytes());
                        }
                    }
                    outputStreamWriter.close();
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("*/*");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                            "Test Subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT,
                            "go on read the emails");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                } catch (JSONException | ParseException | FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        };

        client.get(AddImage.this, ADMINSERVER+ adminUsername,responseHandler);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), UserPageActivity.class);
        String user = getIntent().getStringExtra("USERNAME");
        myIntent.putExtra("USERNAME",user);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        return;
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        return;
    }
}

