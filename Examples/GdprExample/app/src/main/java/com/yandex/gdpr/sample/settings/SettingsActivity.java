package com.yandex.gdpr.sample.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yandex.gdpr.sample.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
