package com.vva.eventcountdown;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    static final String SETTINGS_FILENAME = "settings";
    public final String SETTING_ORDER = "ordering";
    public final String SETTING_FORMAT = "format";
    private SharedPreferences sharedPreferences;

    Menu menu;
    TextView welcomeTextView;
    List<MyEvent> events = new ArrayList<MyEvent>();
    ListView listView;
    EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.listView);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        listView.setOnItemLongClickListener(longClickListener);
        sharedPreferences = getSharedPreferences(SETTINGS_FILENAME, MODE_PRIVATE);
    }

    ListView.OnItemLongClickListener longClickListener = new ListView.OnItemLongClickListener()
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            MyEvent event = eventAdapter.getItem(position);
            if(event!=null)
            {
                Intent intent = new Intent(getApplicationContext(),MyEventActivity.class);
                intent.putExtra("ID",event.getId());
                startActivity(intent);
            }
            return true;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        hideSortMenuItem();
        hideFormatMenuItem();
        return true;
    }

    public void hideSortMenuItem()
    {

        int orderBy = sharedPreferences.getInt(SETTING_ORDER,0);
        switch(orderBy)
        {
            case DateBaseHelper.ORDER_ASC:
            {
                menu.findItem(R.id.action_sort_asc).setEnabled(false);
                menu.findItem(R.id.action_sort_desc).setEnabled(true);
                menu.findItem(R.id.action_sort_nosort).setEnabled(true);
                break;
            }
            case DateBaseHelper.ORDER_DESC:
            {
                menu.findItem(R.id.action_sort_asc).setEnabled(true);
                menu.findItem(R.id.action_sort_desc).setEnabled(false);
                menu.findItem(R.id.action_sort_nosort).setEnabled(true);
                break;
            }
            case DateBaseHelper.ORDER_NONE:
            {
                menu.findItem(R.id.action_sort_asc).setEnabled(true);
                menu.findItem(R.id.action_sort_desc).setEnabled(true);
                menu.findItem(R.id.action_sort_nosort).setEnabled(false);
                break;
            }
        }
        //---------------------
    }

    public void hideFormatMenuItem()
    {

        int format = sharedPreferences.getInt(SETTING_FORMAT,0);
        switch(format)
        {
            case 0:
            {
                menu.findItem(R.id.action_format_dmy).setEnabled(false);
                menu.findItem(R.id.action_format_ymd).setEnabled(true);
                break;
            }
            case 1:
            {
                menu.findItem(R.id.action_format_dmy).setEnabled(true);
                menu.findItem(R.id.action_format_ymd).setEnabled(false);
                break;
            }
        }
        //---------------------
    }

    public void showEvents()
    {
        int orderBy = sharedPreferences.getInt(SETTING_ORDER,0);
        DateBaseAdapter adapter = new DateBaseAdapter(this);
        adapter.open();
        List<MyEvent> events = adapter.getEvents(orderBy,false);
        if(events.isEmpty())
        {
            welcomeTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            welcomeTextView.setVisibility(View.GONE);
        }

        eventAdapter = new EventAdapter(this, R.layout.list_item, events);
        listView.setAdapter(eventAdapter);
        adapter.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_sort_asc)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(SETTING_ORDER,DateBaseHelper.ORDER_ASC);
            editor.apply();
            item.setEnabled(false);
            hideSortMenuItem();
            showEvents();
        }

        if(id == R.id.action_sort_desc)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(SETTING_ORDER,DateBaseHelper.ORDER_DESC);
            editor.apply();
            hideSortMenuItem();
            showEvents();
        }

        if(id == R.id.action_sort_nosort)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(SETTING_ORDER,DateBaseHelper.ORDER_NONE);
            editor.apply();
            hideSortMenuItem();
            showEvents();
        }

        if(id == R.id.action_format_dmy)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(SETTING_FORMAT,0);
            editor.apply();
            hideFormatMenuItem();
            showEvents();
        }

        if(id == R.id.action_format_ymd)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(SETTING_FORMAT,1);
            editor.apply();
            hideFormatMenuItem();
            showEvents();
        }

        if(id == R.id.action_archive)
        {
            Intent intent = new Intent(MainActivity.this,ArcActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void addNewEvent(View v)
    {
        Intent intent = new Intent(MainActivity.this, MyEventActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        showEvents();
    }
}
