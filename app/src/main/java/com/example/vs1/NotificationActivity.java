package com.example.vs1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    TextView tem2;
    TextView oxi2;
    TextView pul2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        tem2 = findViewById(R.id.tem2);
        oxi2 = findViewById(R.id.oxi2);
        pul2 = findViewById(R.id.pul2);

        Intent intent = getIntent();
        String tem = intent.getStringExtra("temperature");
        String oxi = intent.getStringExtra("oximeter");
        String pul = intent.getStringExtra("pulse");

        tem2.setText(tem);
        oxi2.setText(oxi);
        pul2.setText(pul);

//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage("10010", null,
//                    "123", null, null);
//            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
//            Toast.makeText(ShowData.this, "SMS failed, please try again.", Toast.LENGTH_LONG).show();
//                            }

    }
}