package com.example.vs1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Connection connection = null;
    Button login;
    Button reg;
    EditText nameText;
    ResultSet rs;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //隐藏系统默认标题
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null){
//            actionBar.hide();
//        }

        login = findViewById(R.id.login);
        reg = findViewById(R.id.reg);
        nameText = findViewById(R.id.name_login);
        progressBar = findViewById(R.id.progressBar_main);
        progressBar.setVisibility(View.GONE);

        Intent intent = new Intent(getApplicationContext(), LongRunningService.class);
        getApplicationContext().startService(intent);

        login.setOnClickListener(this);
        reg.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String name = nameText.getText().toString();
                new Thread(() -> {
                    Looper.prepare();
                    try {
                        if (!nameText.equals("")) {//判断输入框中是否有数据
                            handler.sendEmptyMessage(11);
                            connection = DBOpenHelper.getConn();
                            if (DBOpenHelper.getExit(connection, "patient_name", name)==1) {
                                connection = DBOpenHelper.getConn();
                                //获取id
                                String sql = "SELECT * FROM patient WHERE patient_name='" + name + "'";
                                rs = DBOpenHelper.getQuery(connection, sql);
                                String id = rs.getString("patient_id");
                                String number = rs.getString("contact_number");
                                //存储用户信息到本地
                                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                                editor.putString("name", name);
                                editor.putString("id", id);
                                editor.apply();
                                //传递id到下一页
                                Intent intent = new Intent(MainActivity.this, ShowData.class);
                                intent.putExtra("idText", id);
                                intent.putExtra("name", name);
                                intent.putExtra("number", number);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Please register first", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(12);
                    DBOpenHelper.closeAll(connection);
                    Looper.loop();
                    MainActivity.this.finish();
                }).start();
                break;
            case R.id.reg:
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String name = data.getStringExtra("name");
                    nameText.setText(name);
                }
                break;
            default:
                break;
        }
    }



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case 12:
                    progressBar.setVisibility(View.GONE);
                default:
                    break;
            }
        }
    };
}