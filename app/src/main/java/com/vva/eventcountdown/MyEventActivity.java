package com.vva.eventcountdown;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Calendar;

public class MyEventActivity extends AppCompatActivity
{
    DateBaseAdapter adapter;
    DatePicker datePicker;
    EditText titleEditText;
    EditText descriptionEditText;
    Button deleteButton;
    TextView oldTextView;
    Calendar c;
    long id = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
                default: return super.onOptionsItemSelected(item);
        }

    }

    public void selectFromCalendar(View v)
    {
        DatePickerDialog dp;
        if(id > 0)
        {
            dp = new DatePickerDialog(MyEventActivity.this,selectDateLinster, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        }
        else
        {
            dp = new DatePickerDialog(MyEventActivity.this,selectDateLinster, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }
        dp.getDatePicker().setMinDate(c.getTimeInMillis());
        dp.show();
    }

    DatePickerDialog.OnDateSetListener selectDateLinster = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            datePicker.init(year,month,dayOfMonth,null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        datePicker = findViewById(R.id.calendarView);
        adapter = new DateBaseAdapter(this);
        c = Calendar.getInstance();
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),null);
        datePicker.setMinDate(c.getTimeInMillis());
        deleteButton = findViewById(R.id.deleteButton);
        oldTextView = findViewById(R.id.oldTextView);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            id = bundle.getLong("ID");
            if(id>0) //существующий элемент
            {
                setTitle(getResources().getString(R.string.newevent_text_label));
                adapter.open();
                MyEvent event = adapter.getEvent(id);
                titleEditText.setText(event.getTitle());
                descriptionEditText.setText(event.getDescription());
                datePicker.init(event.getLocalDate().getYear(), event.getLocalDate().getMonthValue()-1,event.getLocalDate().getDayOfMonth(),null);
                adapter.close();
                deleteButton.setVisibility(View.VISIBLE);

                if(event.localDate.toEpochDay() < LocalDate.now().toEpochDay()) //архивный
                {
                    oldTextView.setText(getResources().getString(R.string.newevent_text_foroldevents,event.getLocalDateTimeString()));
                    oldTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void delete(View v)
    {
        adapter.open();
        adapter.delete(id);
        adapter.close();
        finish();
    }

    public void save(View v)
    {
        if(titleEditText.getText().length() == 0)
        {
            Toast.makeText(MyEventActivity.this,"Поле заголовка обязательно для заполнения",Toast.LENGTH_LONG).show();
            return;
        }

        String title = titleEditText.getText().toString();
        String descr = descriptionEditText.getText().toString();
        int year = datePicker.getYear();
        int mon = datePicker.getMonth()+1;
        int day = datePicker.getDayOfMonth();


        MyEvent event = new MyEvent(id,title,descr,year,mon,day);

        adapter.open();
        if(id>0)
        {
            adapter.update(event);
        }
        else
        {
            adapter.insert(event);
        }
        adapter.close();
        finish();
    }
}
