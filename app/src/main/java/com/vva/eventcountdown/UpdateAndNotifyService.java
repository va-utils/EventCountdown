package com.vva.eventcountdown;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class UpdateAndNotifyService extends Service {
    public UpdateAndNotifyService() {
    }
    private static final String CHANEL_ID = "EVENT_COUNTDOWN_CHANEL";
    private static final int NOTIFY_ID = 377;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase(Intent.ACTION_DATE_CHANGED))
            {
                todayEvents(context);
                updateWidgets(context);
            }
        }
    };

    private void todayEvents(Context context)
    {
        try
        {
            DateBaseAdapter adapter = new DateBaseAdapter(context);
            adapter.open();
            List<MyEvent> events = adapter.getEvents(DateBaseHelper.ORDER_NONE,false);
            adapter.close();
            LocalDate current = LocalDate.now();
            int evs = 0;
            StringBuilder msg = new StringBuilder();

            for (int i = 0; i < events.size(); i++) {
                if (current.isEqual(events.get(i).localDate)) {
                    msg.append(events.get(i).getTitle());
                    msg.append('\n');
                    evs++;
                }
            }

            if (evs > 0) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANEL_ID);
                NotificationChannel channel = new NotificationChannel(CHANEL_ID, "Наступление события", NotificationManager.IMPORTANCE_HIGH);
                builder.setContentText(msg.toString());
                builder.setContentTitle(context.getString(R.string.all_text_today));
                builder.setSmallIcon(R.drawable.ic_stat_name);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
                notificationManager.notify(NOTIFY_ID, builder.build());
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void updateWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName appWidget = new ComponentName(context.getPackageName(), CountDownAppWidget.class.getName());
        int[] ids = appWidgetManager.getAppWidgetIds(appWidget);
        for (int appWidgetId :ids) {
            CountDownAppWidget.updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        //todayEvents(getApplicationContext());
        registerReceiver(receiver,filter);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId )
    {
        updateWidgets(this);
        return super.onStartCommand(intent,flags,startId);
    }
}
