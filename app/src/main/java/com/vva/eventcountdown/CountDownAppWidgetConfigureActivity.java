package com.vva.eventcountdown;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

import java.util.List;

/**
 * The configuration screen for the {@link CountDownAppWidget CountDownAppWidget} AppWidget.
 */
public class CountDownAppWidgetConfigureActivity extends AppCompatActivity implements  ColorPickerDialogListener{



    static final String SETTINGS_FILENAME = "settings";
    private static final String PREF_PREFIX_KEY = "widget_";
    private static final String PREF_PREFIX_KEY_OP = "widget_op_";
    private static final String PREF_PREFIX_KEY_FS = "widget_fs_";
    private static final String PREF_PREFIX_KEY_FC = "widget_fc_";

    public final String SETTING_ORDER = "ordering";
    public static final String SETTING_DEF_COLOR = "default_color";
    public static final String SETTING_DEF_TEXTSIZE = "default_textsize";
    public static final String SETTING_DEF_OPACITY = "default_opacity";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    ListView listView;
    EventAdapter eventAdapter;
    TextView selectedTextView;
    Button addButton;
    SeekBar opSeekBar;
    SeekBar fsSeekBar;
    Button selectColorButton;
    TextView selectColorTextView;
    int color = 0;
    long selectedId;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = CountDownAppWidgetConfigureActivity.this;



            // When the button is clicked, store the string locally
            //String widgetText = mAppWidgetText.getText().toString();
            saveId(context,mAppWidgetId,selectedId,opSeekBar.getProgress(),fsSeekBar.getProgress(),color);
          //  Toast.makeText(context,String.valueOf(fsSeekBar.getProgress()),Toast.LENGTH_LONG).show();
           // addButton.setTextSiz
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
    static void saveId(Context context, int appWidgetId, long value, int opacity, int fs, int color) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(SETTINGS_FILENAME, 0).edit();
       // prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.putLong(PREF_PREFIX_KEY + appWidgetId,value);
        prefs.putInt(PREF_PREFIX_KEY_OP + appWidgetId, opacity);
        prefs.putInt(PREF_PREFIX_KEY_FS + appWidgetId, fs);
        prefs.putInt(PREF_PREFIX_KEY_FC + appWidgetId, color);
        //---сохраним последние пользовательские настройки
        prefs.putInt(SETTING_DEF_COLOR,color);
        prefs.putFloat(SETTING_DEF_TEXTSIZE,fs);
        prefs.putInt(SETTING_DEF_OPACITY, opacity);
        //---
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static long loadId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILENAME, 0);
        return prefs.getLong(PREF_PREFIX_KEY + appWidgetId, -1);
    }

    static int loadColor(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILENAME,0);
        return prefs.getInt(PREF_PREFIX_KEY_FC+appWidgetId,0);
    }

    static float loadOpacity(Context context, long appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILENAME, 0);
        int opacity = prefs.getInt(PREF_PREFIX_KEY_OP+appWidgetId,90);
        return (float)opacity/100;
    }

    static float loadFontSize(Context context, long appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILENAME, 0);
        int fontSize = prefs.getInt(PREF_PREFIX_KEY_FS+appWidgetId,12);
        return (float)fontSize;
    }

    static int loadFormat(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILENAME,0);
        return prefs.getInt("format",0);
    }

    static void deleteId(Context context, long appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(SETTINGS_FILENAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.remove(PREF_PREFIX_KEY_OP + appWidgetId);
        prefs.remove(PREF_PREFIX_KEY_FS + appWidgetId);
        prefs.remove(PREF_PREFIX_KEY_FC + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.count_down_app_widget_configure);

        SharedPreferences sharedPreferences = getSharedPreferences(SETTINGS_FILENAME, 0);



        color = sharedPreferences.getInt(SETTING_DEF_COLOR,Color.DKGRAY);
        float fontsize = sharedPreferences.getFloat(SETTING_DEF_TEXTSIZE,12.0F);

        //---текст с выбранным событием
        selectedTextView = findViewById(R.id.selectedTextView);

        //---ползунок прозрачности
        opSeekBar = findViewById(R.id.opSeekBar);
        int opacity = sharedPreferences.getInt(SETTING_DEF_OPACITY,90);
        opSeekBar.setProgress(opacity);

        //---метка примера текста
        selectColorTextView = findViewById(R.id.selectColorTextView);
        selectColorTextView.setTextColor(color);
        //selectColorTextView.setTextSize(fontsize);

        //---ползунок размера текста
        fsSeekBar = findViewById(R.id.fontSeekBar);
        fsSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        fsSeekBar.setProgress((int)fontsize);

        //---кнопка выбора текста
        selectColorButton = findViewById(R.id.selectColorButton);
        selectColorButton.setOnClickListener(sel);

        //---кнопка добавления виджета
        addButton = findViewById(R.id.addButton);
        addButton.setEnabled(false);
        addButton.setOnClickListener(mOnClickListener);

        //---список с событиями
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //DateBaseAdapter adapter = new DateBaseAdapter(CountDownAppWidgetConfigureActivity.this);
                MyEvent event = eventAdapter.getItem(position);

                if(event!=null)
                {
                    long EventId = event.getId();
                    selectedTextView.setText(getString(R.string.widget_config_text_selectedevent,event.getTitle()));
                    selectedId = EventId;
                    addButton.setEnabled(true);
                }
            }
        });



        //---выведем список событий

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
        }

        //mAppWidgetText.setText(loadTitlePref(CountDownAppWidgetConfigureActivity.this, mAppWidgetId));
    }

    public void createColorPickerDialog()
    {
        ColorPickerDialog.Builder builder = ColorPickerDialog.newBuilder();
        builder.setColor(color);
        builder.setDialogType(ColorPickerDialog.TYPE_PRESETS);
        builder.setAllowCustom(true);
        builder.setAllowPresets(true);
        builder.setColorShape(ColorShape.SQUARE);
        builder.show(this);
    }

    View.OnClickListener sel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createColorPickerDialog();
        }
    };

    @Override
    public void onColorSelected(int dialogId, int color) {
        this.color = color;
        selectColorTextView.setTextColor(color);
    }

    @Override
    public void onDialogDismissed(int dialogId) {
        selectColorTextView.setTextColor(color);
    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            selectColorTextView.setTextSize((float)(seekBar.getProgress()));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    protected void onRestoreInstanceState(Bundle bundle)
    {
        super.onRestoreInstanceState(bundle);
        color = bundle.getInt("FONT_COLOR");
        selectColorTextView.setTextColor(color);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle)
    {
        //TODO: сделать сохранение выбранного события
        bundle.putInt("FONT_COLOR",color);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_count_down_app_widget_configure,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.widget_config_action_defaults)
        {
            setDefaults();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDefaults()
    {
        opSeekBar.setProgress(90);
        fsSeekBar.setProgress(12);
        color=Color.DKGRAY;
        selectColorTextView.setTextColor(color);
    }
}

