
package com.android.franciswairegi.weatherforecast;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public class WeatherForecastDetailPagerActivity extends AppCompatActivity {

    private static final String TAG = "detailPagerActivity";

    private static final String EXTRA_FORECAST_ID = "forecast_id";
    private static final String EXTRA_CITY_ID = "city_id";

    private ViewPager mViewPager;
    private List<WeatherForecastDao.WeatherForecastItemCity> mWeatherForecastItemsByCity;
    //private ArrayList<SectionOrRow> mSectionOrRowItems = new ArrayList<>();
    private ArrayList<WeatherForecastItem> mWeatherForecastItems;

    private String mForecastIdExtra;

    //private WeatherForecastViewModel mWeatherForecastViewModel;


    public static Intent newIntent(Context context, String forecastId, String cityId){
        Intent intent = new Intent(context, WeatherForecastDetailPagerActivity.class);
        intent.putExtra(EXTRA_FORECAST_ID, forecastId);
        intent.putExtra(EXTRA_CITY_ID, cityId);
        return intent;
    }


/*    public static Intent newIntent(
            Context context
            ,ArrayList<WeatherForecastItem> weatherForecastItems){
        Intent intent = new Intent(context, WeatherForecastDetailPagerActivity.class);
        intent.putParcelableArrayListExtra(
                WeatherForecastItem.EXTRA_ARRAY_LIST,weatherForecastItems);

        return intent;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_detail_viewpager);

        Toolbar toolbar = findViewById(R.id.viewpager_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // Enable the Up Button
        //actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setSubtitle("My Subtitle");
        //actionBar.setTitle("My Title");

        mForecastIdExtra =  getIntent().getStringExtra(EXTRA_FORECAST_ID);
        String cityId = getIntent().getStringExtra(EXTRA_CITY_ID);

        Log.i(TAG, "TESTING2 forecastId in detailPager " + mForecastIdExtra);
        Log.i(TAG, "TESTING2 cityId in detailPager " + cityId);

        final WeatherForecastViewModel weatherForecastViewModel;
        weatherForecastViewModel = ViewModelProviders.of(this)
                .get(WeatherForecastViewModel.class);

        Log.i(TAG, "TESTING2 mWeatherForecastViewModel ");

        weatherForecastViewModel.getWeatherForecastItemsByCity(cityId).observe(
                //mWeatherForecastViewModel.getAllWeatherForecast().observe(
                this,
                new Observer<List<WeatherForecastDao.WeatherForecastItemCity>>() {
                    @Override
                    public void onChanged(@Nullable List<WeatherForecastDao.WeatherForecastItemCity> weatherForecastItems) {

                        Log.i(TAG, "TESTING2 mWeatherForecastViewModel observe ");
                        if(!weatherForecastItems.isEmpty()) {

                            mWeatherForecastItemsByCity = weatherForecastItems;

                            Log.i(TAG, " TESTING2 weatherForecastItems.get(0).getCityName()"
                                    + weatherForecastItems.get(0).weatherForecastItem.getCityName());
                            Log.i(TAG, "TESTING2 weatherForecastItems.get(0).weatherForecastCityItem.getCityState()"
                                    + mWeatherForecastItemsByCity.get(0).weatherForecastItem.getForecastId());

                            actionBar.setTitle(mWeatherForecastItemsByCity.get(0).weatherForecastItem.getCityName());

                            String subtitle;
                            if (mWeatherForecastItemsByCity.get(0).getCityState().isEmpty()){
                                subtitle = mWeatherForecastItemsByCity.get(0).getCountryName();
                            } else {
                                subtitle = mWeatherForecastItemsByCity.get(0).getCityState() + ", " +
                                        mWeatherForecastItemsByCity.get(0).getCountryName();
                            }
                            actionBar.setSubtitle(subtitle);

                            mViewPager = findViewById(R.id.forecast_view_pager);

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
                                @Override
                                public Fragment getItem(int position) {
                                    WeatherForecastDao.WeatherForecastItemCity weatherForecastItem
                                            = mWeatherForecastItemsByCity.get(position);
                                    String forecastId =
                                            weatherForecastItem.weatherForecastItem.getForecastId();
                                    return WeatherForecastDetailFragment.newInstance(
                                            forecastId);

                                }

                                @Override
                                public int getCount() {
                                    Log.i(TAG,"Size of Array in Pager Activity "+
                                            mWeatherForecastItemsByCity.size());
                                    return mWeatherForecastItemsByCity.size();
                                }
                            });


                            // ** To be uncommented later
                            for (int i = 0 ; i < mWeatherForecastItemsByCity.size(); i++){
                                if (mWeatherForecastItemsByCity.get(i).
                                        weatherForecastItem.getForecastId().equals(mForecastIdExtra)){
                                    mViewPager.setCurrentItem(i);
                                    break;
                                }
                            }


                        }
                    }
                });



/*        mWeatherForecastItems = getIntent().getParcelableArrayListExtra(
                WeatherForecastItem.EXTRA_ARRAY_LIST);*/




/*Log.i("Tag","mSectionOrRowItems size " + mSectionOrRowItems.size());
        Log.i("Tag","mSectionOrRowItems weather description  " +
                mSectionOrRowItems.get(1).getRow().getWeatherDescription());*/

    }
}

