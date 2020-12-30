package com.example.vs1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowData extends AppCompatActivity {

    Connection connection = null;
    TextView tem;
    TextView oxi;
    TextView pul;
    TextView showid;
    TextView time;
    TextView name;
    private ResultSet rs = null;
    private boolean run = false;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //显示数据
                    try {
                        tem.setText(rs.getString("temperature"));
                        oxi.setText(rs.getString("oxygen_content"));
                        pul.setText(rs.getString("pulse"));
                        time.setText(rs.getString("time"));
                        name.setText(rs.getString("patient_name"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        tem = findViewById(R.id.tem);
        oxi = findViewById(R.id.oxi);
        pul = findViewById(R.id.pul);
        showid = findViewById(R.id.showid);
        time = findViewById(R.id.time);
        name = findViewById(R.id.name);

        //接收MainActivity的idText
        Intent intent = getIntent();
        String id = intent.getStringExtra("idText");
        showid.setText(id);

        //实现定时刷新
        run = true;
        handler.postDelayed(task, 1000/10);

    }

    public void ShowData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    connection = DBOpenHelper.getConn();
                    String id = showid.getText().toString();
                    String sql = "SELECT * FROM patient_"+id+" a, patient b WHERE a.p_id = b.patient_id ORDER BY time DESC";
                    rs = DBOpenHelper.getQuery(connection, sql);
                    if (rs == null) {//判断是否存在patient_id表
                        Toast.makeText(ShowData.this, "暂无数据", Toast.LENGTH_LONG).show();
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();
    }

    //设置右上角菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //菜单点击内容
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.history:
                String idText = showid.getText().toString();
                Intent intent = new Intent(ShowData.this, HistoryData.class);
                intent.putExtra("idText", idText);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    //定时刷新，10s
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (run) {
                ShowData();
                DBOpenHelper.closeAll(connection);
                handler.postDelayed(this, 1000*10);
            }
        }
    };

//    public void emptyData(TextView id) {
//        if (id.getText().toString().equals("")) {
//            id.setText("无数据");
//        }
//    }
}