package com.abdallahn_ameerh.ex3;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import static com.abdallahn_ameerh.ex3.EditorActivity.notificationManager;


public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "channel_main";
    private int notificationID ;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("debug", "onReceive: notificaion !!! ");
        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
        notify(context);



    }
    public void notify(Context context)
    {
        String title = "TODO NOTIFICATION";
        String text = "Click me!";

        Intent intent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        // 3. Create & show the Notification. on Build.VERSION < OREO notification avoid CHANEL_ID
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(notificationID, notification);
    }
}
