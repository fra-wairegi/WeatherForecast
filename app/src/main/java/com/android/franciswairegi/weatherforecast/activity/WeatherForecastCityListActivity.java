package com.android.franciswairegi.weatherforecast.activity;

import android.os.Bundle;

import com.android.franciswairegi.weatherforecast.R;
import com.android.franciswairegi.weatherforecast.fragment.WeatherForecastCityListFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class WeatherForecastCityListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast_city);

        Toolbar toolbar = findViewById(R.id.city_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // Enable the Up Button
        actionBar.setDisplayShowTitleEnabled(false); // Hide title

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.city_fragment_container);

        if (fragment == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.city_fragment_container,
                            WeatherForecastCityListFragment.newInstance())
                    .commit();
        }
    }
}
