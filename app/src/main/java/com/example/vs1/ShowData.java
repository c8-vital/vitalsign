package com.example.vs1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
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
    String temperature;
    String oximeter;
    String pulse;
    private ResultSet rs = null;
    private boolean run = true;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //显示数据
                    try {
                        tem.setText(rs.getString("temperature") + " ℃");
                        oxi.setText(rs.getString("oxygen_content") + " %");
                        pul.setText(rs.getString("pulse") + " bpm");
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
        CheckData();
        //检查数据是否异常

        handler.postDelayed(task, 1000 / 10);
    }

//    public void ShowData(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                try {
//                    connection = DBOpenHelper.getConn();
//                    String id = showid.getText().toString();
//                    String sql = "SELECT * FROM patient_"+id+" a, patient b WHERE a.p_id = b.patient_id ORDER BY time DESC";
//                    rs = DBOpenHelper.getQuery(connection, sql);
//                    handler.sendEmptyMessage(1);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Looper.loop();
//            }
//        }).start();
//    }

    //血氧异常通知
    public void CheckData(){
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Looper.prepare();
                try {
                    connection = DBOpenHelper.getConn();
                    String id = showid.getText().toString();
                    String sql = "SELECT * FROM patient_"+id+" a, patient b WHERE a.p_id = b.patient_id ORDER BY time DESC";
                    rs = DBOpenHelper.getQuery(connection, sql);
                    if (rs == null) {//判断是否存在patient_id表
                        Toast.makeText(ShowData.this, "No data available", Toast.LENGTH_LONG).show();
                        System.out.println("No data available");
                    } else {
                        handler.sendEmptyMessage(1);
                        temperature = rs.getString("temperature");
                        oximeter = rs.getString("oxygen_content");
                        pulse = rs.getString("pulse");
                        if (Double.parseDouble(oximeter) < 95) {
                            run = false;
                            Intent intent1 = new Intent(ShowData.this, NotificationActivity.class);
                            intent1.putExtra("temperature" ,temperature);
                            intent1.putExtra("oximeter", oximeter);
                            intent1.putExtra("pulse", pulse);
//                            PendingIntent pi = PendingIntent.getActivity(ShowData.this, 0, intent1, 0);
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            Notification notification;
                            NotificationChannel channel = new NotificationChannel("1", "description", NotificationManager.IMPORTANCE_HIGH);
                            manager.createNotificationChannel(channel);
                            notification = new Notification.Builder(ShowData.this, "1")
                                    .setCategory(Notification.CATEGORY_MESSAGE)
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setContentTitle("Data exception warning")
                                    .setContentText("Blood oxygen saturation is too low, please contact medical staff in time.")
                                    .setWhen(System.currentTimeMillis())
                                    .setVibrate(new long[]{0, 1000, 1000, 1000})
                                    .setAutoCancel(true)
//                                    .setContentIntent(pi)
                                    .build();
                            manager.notify(1, notification);
                            startActivity(intent1);
                        }
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

    //定时刷新，5s
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (run) {
                CheckData();
                DBOpenHelper.closeAll(connection);
                handler.postDelayed(this, 1000*5);
            }
        }
    };

//    public void emptyData(TextView id) {
//        if (id.getText().toString().equals("")) {
//            id.setText("无数据");
//        }
//    }
}