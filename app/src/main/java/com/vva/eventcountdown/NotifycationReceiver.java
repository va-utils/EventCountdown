package com.vva.eventcountdown;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.time.LocalDate;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotifycationReceiver extends BroadcastReceiver {
    private static final String CHANEL_ID = "EVENT_COUNTDOWN_CHANEL";
    private static final int NOTIFY_ID = 377;

    public NotifycationReceiver() {}
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equalsIgnoreCase(Intent.ACTION_DATE_CHANGED)) {
            Intent forServiceIntent = new Intent(context, UpdateAndNotifyService.class);
            context.startService(forServiceIntent);
        }
    }
}
