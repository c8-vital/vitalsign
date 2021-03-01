package com.example.vs1;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class LongRunningService extends Service {
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
                Looper.prepare();
//                Toast.makeText(getApplicationContext(), "123123", Toast.LENGTH_LONG).show();
                sendNotification();
                Looper.loop();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60*60*1000;   //一小时的毫秒数
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
}