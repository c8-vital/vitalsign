package com.example.vs1;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.sql.Connection;
import java.sql.ResultSet;

public class LongRunningService extends Service {
    String timeT;
    public LongRunningService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                String name = pref.getString("name", "null");
                String id = pref.getString("id", "null");
                String table = pref.getString("table", "null");
                Looper.prepare();
                //如果有本地user数据
                if (!id.equals("null") & !table.equals("null")) {
                    String timeN = TimeUtils.getNowTime();
                    String timeM = TimeUtils.getNowDate() + "08:00:00";
                    timeT = getTimeT(id);
//                    timeT = "2021-03-08 06:00:00";
                    String TD = TimeUtils.getTD(timeN, timeT);
                    String hours = null;


                    if (TD.startsWith("h", 1)) {
                        hours = TD.substring(0, 1);
                    } else if (TD.startsWith("h", 2)) {
                        hours = TD.substring(0, 2);
                    } else if (TD.startsWith("h", 3)) {
                        hours = TD.substring(0, 3);
                    } else if (TD.startsWith("h", 4)) {
                        hours = TD.substring(0, 4);
                    } else if (TD.startsWith("h", 5)) {
                        hours = TD.substring(0, 5);
                    }
                    //判断现在时间是否早于8:00:00
                    if (TimeUtils.isDateOneBigger(timeM, timeN)) {
                        //早于8:00:00
                        if (Integer.parseInt(hours) >= 12) {
                            sendNotification();
                            Toast.makeText(getApplicationContext(), "User:"+ name + "\n"+ TD + " have passed since the last measurement.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (Integer.parseInt(hours) >= 6) {
                            sendNotification();
                            Toast.makeText(getApplicationContext(), "User:"+ name + "\n"+ TD + " have passed since the last measurement.", Toast.LENGTH_LONG).show();
                        }
                    }

                }

                Looper.loop();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 5*60*1000;   //5mins的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, LongRunningService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification() {
        //创建数据通知
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("2", "description", NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);
        Notification notification = new Notification.Builder(getApplicationContext(), "2")
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_baseline_add_24))
                .setContentTitle("Please measure your signs data in time")
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long[]{0, 1000, 1000, 1000})
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(new Notification.BigTextStyle().bigText(""))
                .build();
        manager.notify(2, notification);

    }

    public String getTimeT(String id) {
            try {
                Connection connection = DBOpenHelper.getConn();
                String sql = "SELECT * FROM patient_"+id+" ORDER BY time DESC";
                ResultSet rs = DBOpenHelper.getQuery(connection, sql);
                if (rs != null) {//判断是否存在patient_id表
                    timeT = rs.getString("time");
                } else {
                    Toast.makeText(getApplicationContext(), "user_id:"+id+" no data.", Toast.LENGTH_LONG).show();
                }
                DBOpenHelper.closeAll(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return timeT;
    }


}