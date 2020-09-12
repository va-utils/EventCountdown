package com.vva.eventcountdown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ArcActivity extends AppCompatActivity {

    ListView listView;
    EventAdapter eventAdapter;
    TextView welcomeTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.listView);
        listView.setOnItemLongClickListener(longClickListener);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        showOldEvents();
    }

    public void showOldEvents()
    {
        DateBaseAdapter adapter = new DateBaseAdapter(this);
        adapter.open();
        List<MyEvent> events = adapter.getEvents(DateBaseHelper.ORDER_DESC,true);
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
    protected void onResume()
    {
        super.onResume();
        showOldEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_arc_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.arc_activity_clr)
        {
            DateBaseAdapter adapter = new DateBaseAdapter(this);
            adapter.open();
            List<MyEvent> events = adapter.getEvents(DateBaseHelper.ORDER_DESC,true);
            if(events.isEmpty())
            {
                Toast.makeText(this, getString(R.string.arc_text_empty), Toast.LENGTH_SHORT).show();
            }
            else
            {
                long deleted = adapter.deleteOld();
                if(deleted > 0)
                {
                    Toast.makeText(this, getString(R.string.arc_text_result,deleted), Toast.LENGTH_LONG).show();
                    welcomeTextView.setVisibility(View.VISIBLE);
                }

            }
            eventAdapter = new EventAdapter(this, R.layout.list_item, events);
            listView.setAdapter(eventAdapter);
            adapter.close();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
