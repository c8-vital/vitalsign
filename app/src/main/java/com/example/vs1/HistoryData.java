package com.example.vs1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.os.Message;
import android.service.autofill.UserData;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class HistoryData extends AppCompatActivity {
    Connection connection = null;
    RecyclerView historyData;
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    ArrayList<User> list = new ArrayList<User>();


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    UserAdapter adapter = new UserAdapter(list);
                    historyData.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_data);
        //隐藏系统默认标题
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        initUser();
        historyData = findViewById(R.id.historyData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        historyData.setLayoutManager(layoutManager);
    }

    private void initUser(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                //接收ShowData的id
                Intent intent2 = getIntent();
                String idText = intent2.getStringExtra("idText");
                try {
                    String sql = "SELECT * FROM patient_"+idText+" ORDER BY time DESC LIMIT 0,50";
                    connection = DBOpenHelper.getConn();
                    ps = connection.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()){
                            User u = new User();
                            u.setId(rs.getString("p_id"));
                            u.setTem(rs.getString("temperature"));
                            u.setOxi(rs.getString("oxygen_content"));
                            u.setPul(rs.getString("pulse"));
                            u.setTime(rs.getString("time"));
                            list.add(u);
                        }
                        handler.sendEmptyMessage(1);
                    } else {
                        Toast.makeText(HistoryData.this, "No data available", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DBOpenHelper.closeAll(connection);
                Looper.loop();
            }
        }).start();

    }
}