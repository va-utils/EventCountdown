package com.vva.eventcountdown;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventAdapter extends ArrayAdapter<MyEvent>
{
    private List<MyEvent> events;
    private LayoutInflater inflater;
    private int layout;
   // private SharedPreferences sharedPreferences;
    private int format;

    private static final String SETTINGS_FILENAME = "settings";
    //private final String SETTING_FORMAT = "format";

    public EventAdapter(Context context, int resource, List<MyEvent> events)
    {
        super(context, resource, events);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTINGS_FILENAME, context.MODE_PRIVATE);
        format = sharedPreferences.getInt("format",0);
        this.events = events;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {

        if(convertView==null)
        {
            convertView = inflater.inflate(layout,parent, false);
        }
        Resources res = convertView.getResources();

        MyEvent event = events.get(position);

        TextView titleTextView = convertView.findViewById(R.id.list_title);
        TextView descriptionTextView = convertView.findViewById(R.id.list_description);
        TextView periodTextView = convertView.findViewById(R.id.list_period);
        TextView totalDaysTextView = convertView.findViewById(R.id.total_days);

        int totalDays = (int) ChronoUnit.DAYS.between(LocalDate.now(),event.localDate);

        totalDaysTextView.setText(res.getQuantityString(R.plurals.days, Math.abs(totalDays), Math.abs(totalDays)));
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
                    periodTextView.setText(res.getString(R.string.all_remained,sDays,sMonths,sYears));
                else
                    periodTextView.setText(res.getString(R.string.all_remained,sYears,sMonths,sDays));
            else
                if(format==1)
                    periodTextView.setText(res.getString(R.string.all_passed,sDays,sMonths,sYears));
                else
                    periodTextView.setText(res.getString(R.string.all_passed,sYears,sMonths,sDays));
        }
        else
        {
            periodTextView.setText(res.getString(R.string.all_text_today));
        }

        titleTextView.setText(event.getTitle());
        if(event.getDescription().isEmpty())
        {
            descriptionTextView.setVisibility(View.GONE);
        }
        else
        {
            descriptionTextView.setVisibility(View.VISIBLE);
            descriptionTextView.setText(event.getDescription());
        }

        return convertView;
    }
}