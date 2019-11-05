package com.example.proj;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationHelper notificationHelper;
    public class NotificationHelper extends ContextWrapper {
        private NotificationManager mManager;
        public static final String MY_CHANNEL_ID = "com.example.mkaya.lab8.mychannelid";
        public static final String MY_CHANNEL_NAME = "LAB8 CHANNEL";
        public NotificationHelper(Context base) {
            super(base);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                createChannels();
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        private void createChannels() {
            NotificationChannel androidChannel = new NotificationChannel(MY_CHANNEL_ID,
                    MY_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            androidChannel.enableLights(true);
            androidChannel.enableVibration(true);
            androidChannel.setLightColor(Color.GREEN);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(androidChannel);
        }
        public NotificationCompat.Builder getNotification(String title, String time) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext(), MY_CHANNEL_ID)
                            .setContentTitle(title)
                            .setContentText(time).setSmallIcon(R.drawable.ic_notification).setAutoCancel(true);
            return mBuilder;
        }
        private NotificationManager getManager() {
            if (mManager == null) {
                mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }
            return mManager;
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String title=intent.getStringExtra("title");
        String time=intent.getStringExtra("time");
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();
        notificationHelper=new NotificationHelper(context);
        NotificationCompat.Builder mBuilder =notificationHelper.getNotification(title,time);
        notificationHelper.getManager().notify(0, mBuilder.build());
    }
}
