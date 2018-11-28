package com.android.franciswairegi.weatherforecast;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

public class WeatherForecastMainActivity extends AppCompatActivity {

    public static final String TAG = "WeatherMainActivity";


/*    public static final String EXTRA_CITY_ID =
            "com.android.franciswairegi.weatherforecast.city_id";

    private String mCityId;*/

    // Not Used
/*        public static Intent newIntent(Context packageContext, String cityID) {
        Intent intent = new Intent(packageContext, WeatherForecastMainActivity.class);
        Log.i(TAG, "CityId in newIntent " + cityID);
        intent.putExtra(EXTRA_CITY_ID, cityID);
        return intent;
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "TESTING1 on Pause() ");

    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "on Activity onActivityResult ");
        Log.i(TAG, "data intent " +
                data.getIntExtra(WeatherForecastCityListFragment.EXTRA_CITY_ID,0));

    }*/

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "TESTING1 on Activity onResume() ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i(TAG, "TESTING1 onCreateCalled");

 /*       String cityID = getIntent().getStringExtra(EXTRA_CITY_ID);

        if (cityID == null) {
            mCityId = "5358705"; // Default, Huntington beach. To be changed to current location
        } else {
            mCityId = cityID; // CityId selected from a list of cities.
        }

        Log.i(TAG, "cityID " + cityID);
        //mCityId = "5358705";*/

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        /*
        ** Check if the fragment exists
        * particularly important in ensuring setRetainInstance in the fragment
        * successfully saves the instance on configuration changes
         */
        if(fragment == null) {
            Log.i(TAG, "fragment == null ");
            fm.beginTransaction()
                    //.add(R.id.fragment_container, WeatherForecastFragment.newInstance(mCityId))
                    .add(R.id.fragment_container, WeatherForecastFragment.newInstance())
                    .commit();
        }
    }

}
