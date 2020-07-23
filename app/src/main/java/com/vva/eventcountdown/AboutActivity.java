package com.vva.eventcountdown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView versionTextView = findViewById(R.id.versionTextView);
        versionTextView.setText(getResources().getString(R.string.about_version,BuildConfig.VERSION_NAME));
       // Toast.makeText(getApplicationContext(),String.valueOf(LocalDate.now().toEpochDay()),Toast.LENGTH_LONG).show();
    }
}
