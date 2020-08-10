package com.vva.eventcountdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotifycationReceiver extends BroadcastReceiver {

    public NotifycationReceiver() {}
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equalsIgnoreCase(Intent.ACTION_DATE_CHANGED)) {
            Intent forServiceIntent = new Intent(context, UpdateAndNotifyService.class);
            context.startService(forServiceIntent);
        }
    }
}
