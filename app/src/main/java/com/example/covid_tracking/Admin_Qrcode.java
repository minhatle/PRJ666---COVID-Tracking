package com.example.covid_tracking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Admin_Qrcode extends AppCompatActivity {
    ImageView qrImage;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.admin_qrcode);
        qrImage = findViewById(R.id.qrImage);
        QRCodeWriter writer = new QRCodeWriter();
        try {
            String content = getIntent().getStringExtra("ADMINDATA");
            JSONObject adminData = new JSONObject(content);
            JSONArray userArray = adminData.getJSONArray("users");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currDate = new Date();
            int count = 0;
            for (int i = 0; i < userArray.length(); i++) {
               Date tempDate = sdf.parse(userArray.getJSONObject(i).getString("date"));
               SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                if (fmt.format(currDate).equals(fmt.format(tempDate))){
                    count++;
                }
            }
            TextView adminName = findViewById(R.id.userNameAdmin);
            adminName.setText(adminData.getString("userName"));
            TextView numUsers = findViewById(R.id.numUsers);
            numUsers.setText(String.valueOf(count));
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrImage.setImageBitmap(bmp);

        } catch (WriterException | JSONException | ParseException e) {
            e.printStackTrace();
        }


    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Admin_Login.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


}

