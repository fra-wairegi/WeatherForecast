package com.android.franciswairegi.weatherforecast.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

import com.android.franciswairegi.weatherforecast.R;
import com.android.franciswairegi.weatherforecast.fragment.WeatherForecastFragment;

public class WeatherForecastMainActivity extends AppCompatActivity {

    /**
     * In this WeatherForecast app, 5 day weather forecast is listed in 3-hour intervals
     * The data is obtained from  {@link https://openweathermap.org/forecast5} API.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        /*
        ** Check if the fragment exists
        * particularly important in ensuring setRetainInstance in the fragment
        * successfully saves the instance on configuration changes
         */
        if(fragment == null) {
            fm.beginTransaction()
                    .add(R.id.fragment_container, WeatherForecastFragment.newInstance())
                    .commit();
        }
    }

}
