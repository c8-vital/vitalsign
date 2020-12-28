package com.example.vs1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import android.service.autofill.UserData;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_data);
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
                    String sql = "SELECT * FROM patient_"+idText+" ORDER BY time DESC LIMIT 0,20";
                    connection = DBOpenHelper.getConn();
                    ps = connection.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()){
                            User u = new User(null, 0, 0, 0, null);
                            u.setId(rs.getString("p_id"));
                            u.setTem(rs.getDouble("temperature"));
                            u.setOxi(rs.getDouble("oxygen_content"));
                            u.setPul(rs.getDouble("pulse"));
                            u.setTime(rs.getString("time"));
                            list.add(u);
                        }
                        UserAdapter adapter = new UserAdapter(list);
                        historyData.setAdapter(adapter);
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