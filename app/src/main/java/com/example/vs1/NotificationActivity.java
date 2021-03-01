package com.example.vs1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationActivity extends AppCompatActivity {

    TextView tem2;
    TextView oxi2;
    TextView pul2;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        tem2 = findViewById(R.id.tem2);
        oxi2 = findViewById(R.id.oxi2);
        pul2 = findViewById(R.id.pul2);
        send = findViewById(R.id.send);

        User user = (User) getIntent().getSerializableExtra("user");
        String tem = user.getTem();
        String oxi = user.getOxi();
        String pul = user.getPul();
        String number = user.getNumber();

        tem2.setText(tem + " ℃");
        oxi2.setText(oxi + " %");
        pul2.setText(pul + " bpm");

        if (Double.parseDouble(oxi)<90) {
            oxi2.setTextColor(Color.parseColor("#FF0000"));
        }
        if (tem!=null && Double.parseDouble(tem)>37.3) {
            tem2.setTextColor(Color.parseColor("#FF0000"));
        }

        send.setOnClickListener(v -> {
            String message = "Abnormal signs detected.\n" + "Temperature:" + tem + "℃  SaO2:" + oxi + "%  pulse:" + pul + "rpm";
            int permissionCheck = ContextCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.SEND_SMS);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(NotificationActivity.this, new String[]{Manifest.permission.SEND_SMS}, 0);
            }
            if (number == null) {
                Toast.makeText(this, "No contact phone set.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, message, null, null);
                    Toast.makeText(NotificationActivity.this, "SMS sent.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(NotificationActivity.this,
                            "SMS failed, please try again.",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });



    }

}