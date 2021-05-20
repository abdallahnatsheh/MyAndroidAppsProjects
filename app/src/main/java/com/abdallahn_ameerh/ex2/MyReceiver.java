package com.abdallahn_ameerh.ex2;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import static  com.abdallahn_ameerh.ex2.MainActivity.notificationManager;
import androidx.core.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver
{
    private static final String CHANNEL_ID = "channel_main";
    private int notificationID;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        boolean notified = false;
        String action = intent.getAction();

        // get battery level from the received Intent
        if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
            // CHECK Are we charging / charged?
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
            // CHECK How are we charging?
            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
            int batteryLevel = intent.getIntExtra("level", 0);
            if (batteryLevel <= 10 && !isCharging && !usbCharge && !acCharge)
                //Log.d("bat", "onReceive: battery!!!");
                if(!notified){
                    notify(context);
                    notified = true;
                }
        }
    }
    public void notify(Context context)
    {
        String title = "Battery Status!!!!";
        String text = "CHARGE ME NOW!!";

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        // 3. Create & show the Notification. on Build.VERSION < OREO notification avoid CHANEL_ID
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_battery_alert_24)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(notificationID, notification);
    }

}
