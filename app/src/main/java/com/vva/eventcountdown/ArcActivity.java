package com.vva.eventcountdown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
}
