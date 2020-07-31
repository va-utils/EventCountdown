package com.vva.eventcountdown;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.RemoteViews;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CountDownAppWidgetConfigureActivity CountDownAppWidgetConfigureActivity}
 */
public class CountDownAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Resources res = context.getResources();
        DateBaseAdapter adapter = new DateBaseAdapter(context);
        adapter.open();
        MyEvent event = adapter.getEvent(CountDownAppWidgetConfigureActivity.loadId(context,appWidgetId));
        adapter.close();

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.count_down_app_widget);
        //основные характеристики виджета
        int format = CountDownAppWidgetConfigureActivity.loadFormat(context);
        int backgroundColor = 0x00f0f0f0; //цвет фона
        int titleColor = 0x00fe943c; //цвет заголовка
        float op = CountDownAppWidgetConfigureActivity.loadOpacity(context,appWidgetId); //непрозрачность
        float baseFontSize = CountDownAppWidgetConfigureActivity.loadFontSize(context,appWidgetId); //размер шрифта
        int color = CountDownAppWidgetConfigureActivity.loadColor(context,appWidgetId);
        //-----

        views.setFloat(R.id.titleTextView,"setTextSize",baseFontSize+2);
        views.setFloat(R.id.countDownTextView,"setTextSize",baseFontSize);
        views.setFloat(R.id.fullCountDownTextView,"setTextSize",baseFontSize);
        views.setInt(R.id.countDownTextView,"setTextColor",color);
        views.setInt(R.id.fullCountDownTextView,"setTextColor",color);
        views.setInt( R.id.linearLayout, "setBackgroundColor", (int)(op * 0xFF) << 24 | backgroundColor);
        views.setInt( R.id.titleTextView, "setBackgroundColor", (int)(op * 0xFF) << 24 | titleColor);

        if(event!=null)
        {
            int totalDays = (int) ChronoUnit.DAYS.between(LocalDate.now(),event.localDate);
            String result = "";

            if(totalDays != 0)
            {
                String sDays = "";
                String sMonths = "";
                String sYears = "";

                Period p = event.getPeriod();
                if(p.getDays() != 0)
                    sDays =  res.getQuantityString(R.plurals.days,Math.abs(p.getDays()),Math.abs(p.getDays()));
                if(p.getMonths() != 0)
                    sMonths = res.getQuantityString(R.plurals.months, Math.abs(p.getMonths()), Math.abs(p.getMonths()));
                if(p.getYears() !=0)
                    sYears = res.getQuantityString(R.plurals.years, Math.abs(p.getYears()), Math.abs(p.getYears()));

                if(totalDays>0)
                    if(format==0)
                        result = res.getString(R.string.all_remained,sDays,sMonths,sYears);
                    else
                        result = res.getString(R.string.all_remained,sYears,sMonths,sDays);
                else
                    if(format==1)
                        result = res.getString(R.string.all_passed,sDays,sMonths,sYears);
                    else
                        result = res.getString(R.string.all_passed,sYears,sMonths,sDays);
            }
            else
            {
                result = context.getString(R.string.all_text_today);
            }


            views.setTextViewText(R.id.titleTextView, event.getTitle());
            views.setTextViewText(R.id.countDownTextView, res.getQuantityString(R.plurals.days, Math.abs(totalDays), Math.abs(totalDays)));
            views.setTextViewText(R.id.fullCountDownTextView,result);
        }
        else
        {
            //CountDownAppWidgetConfigureActivity.deleteId(context,appWidgetId);
            views.setTextViewText(R.id.titleTextView, "Событие удалено");
            views.setTextViewText(R.id.countDownTextView, "");
            views.setTextViewText(R.id.fullCountDownTextView,"");
        }

        Intent forMainActivityIntent = new Intent(context,MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context,appWidgetId,forMainActivityIntent,0);
        views.setOnClickPendingIntent(R.id.linearLayout,pIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }


//        Intent intent = new Intent(context, UpdateAndNotifyService.class);
//        context.startService(intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            CountDownAppWidgetConfigureActivity.deleteId(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

