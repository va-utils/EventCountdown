package com.vva.eventcountdown;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * The configuration screen for the {@link CountDownAppWidget CountDownAppWidget} AppWidget.
 */
public class CountDownAppWidgetConfigureActivity extends Activity {

    static final String SETTINGS_FILENAME = "settings";
    private static final String PREF_PREFIX_KEY = "widget_";
    public final String SETTING_ORDER = "ordering";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    ListView listView;
    EventAdapter eventAdapter;
    TextView selectedTextView;
    Button addButton;
    long selectedId;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = CountDownAppWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            //String widgetText = mAppWidgetText.getText().toString();
            saveId(context,mAppWidgetId,selectedId);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            CountDownAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public CountDownAppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveId(Context context, int appWidgetId, long value) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(SETTINGS_FILENAME, 0).edit();
       // prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.putLong(PREF_PREFIX_KEY + appWidgetId,value);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static long loadId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILENAME, 0);
        long titleId = prefs.getLong(PREF_PREFIX_KEY + appWidgetId, -1);
        return titleId;
    }

    static void deleteId(Context context, long appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(SETTINGS_FILENAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.count_down_app_widget_configure);
        //mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        listView = findViewById(R.id.listView);
        selectedTextView = findViewById(R.id.selectedTextView);
        addButton = findViewById(R.id.addButton);
        addButton.setEnabled(false);
        addButton.setOnClickListener(mOnClickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DateBaseAdapter adapter = new DateBaseAdapter(CountDownAppWidgetConfigureActivity.this);
                MyEvent event = eventAdapter.getItem(position);

                if(event!=null)
                {
                    long EventId = event.getId();
                    selectedTextView.setText("Выбрано событие: " + event.getTitle());
                    selectedId = EventId;
                    addButton.setEnabled(true);
                }
            }
        });


        //---выведем список событий
        SharedPreferences sharedPreferences = getSharedPreferences(SETTINGS_FILENAME, 0);
        int orderBy = sharedPreferences.getInt(SETTING_ORDER,0);
        DateBaseAdapter adapter = new DateBaseAdapter(this);
        adapter.open();
        List<MyEvent> events = adapter.getEvents(orderBy,false);
        eventAdapter = new EventAdapter(this, R.layout.list_item, events);
        listView.setAdapter(eventAdapter);
        adapter.close();
        //----

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        //mAppWidgetText.setText(loadTitlePref(CountDownAppWidgetConfigureActivity.this, mAppWidgetId));
    }
}

