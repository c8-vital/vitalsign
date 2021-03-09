package com.example.vs1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowData extends AppCompatActivity {

    Connection connection = null;
    TextView tem = null;
    TextView oxi = null;
    TextView pul = null;
    TextView showid;
    TextView time;
    TextView name;
    String number = null;
    String id;
    String name1;
    private ResultSet rs = null;
    private boolean run = true;
    User user = new User();

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
        id = intent.getStringExtra("idText");
        name1 = intent.getStringExtra("name");
        number = intent.getStringExtra("number");
        showid.setText(id);
        CheckData();
        //检查数据是否异常
        handler.postDelayed(task, 1000 / 10);
    }

    @Override
    protected void onStop() {
        super.onStop();
        run = false;
        DBOpenHelper.closeAll(connection);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        run = true;
        handler.postDelayed(task, 1000 / 10);
    }

    //血氧异常通知
    public void CheckData(){
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Looper.prepare();
                try {
                    connection = DBOpenHelper.getConn();
                    String sql = "SELECT * FROM patient_"+id+" a, patient b WHERE a.p_id = b.patient_id ORDER BY time DESC";
                    rs = DBOpenHelper.getQuery(connection, sql);
                    if (rs == null) {//判断是否存在patient_id表
                        Toast.makeText(ShowData.this, "No data available", Toast.LENGTH_LONG).show();
                        System.out.println("No data available");
                    } else {
                        user.setId(rs.getString("p_id"));
                        user.setTem(rs.getString("temperature"));
                        user.setOxi(rs.getString("oxygen_content"));
                        user.setPul(rs.getString("pulse"));
                        user.setTime(rs.getString("time"));
                        user.setName(name1);
                        user.setNumber(number);
                        handler.sendEmptyMessage(1);

                        if (Double.parseDouble(user.getOxi()) < 90) {
                            //停止刷新数据
                            run = false;
                            sendNotification();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification() {
        Intent intent1 = new Intent(this, NotificationActivity.class);
        //传递数据到NotificationActivity
        intent1.putExtra("user", user);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        //创建数据通知
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("1", "description", NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);

        Notification notification = new Notification.Builder(ShowData.this, "1")
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_baseline_add_24))
                .setContentTitle("Data exception warning")
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long[]{0, 1000, 1000, 1000})
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pi)
                .setStyle(new Notification.BigTextStyle().bigText("Blood oxygen saturation is too low, please contact medical staff in time.\nClick to send sign data to contact number"))
//                .setStyle(new Notification.BigTextStyle().setBigContentTitle("Data exception warning"))
//                .setStyle(new Notification.BigTextStyle().setSummaryText("Click to send sign data to contact number"))
                .build();
        manager.notify(1, notification);

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
//                DBOpenHelper.closeAll(connection);
                handler.postDelayed(this, 1000*5);
            }
        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //显示数据
                    try {
                        tem.setText(user.getTem() + " ℃");
                        oxi.setText(user.getOxi() + " %");
                        pul.setText(user.getPul() + " bpm");
                        time.setText(user.getTime());
                        name.setText(user.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

}