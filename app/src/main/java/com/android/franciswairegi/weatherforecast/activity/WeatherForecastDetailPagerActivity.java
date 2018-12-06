
package com.android.franciswairegi.weatherforecast.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.franciswairegi.weatherforecast.R;
import com.android.franciswairegi.weatherforecast.model.WeatherForecastItem;
import com.android.franciswairegi.weatherforecast.viewmodel.WeatherForecastViewModel;
import com.android.franciswairegi.weatherforecast.dao.WeatherForecastDao;
import com.android.franciswairegi.weatherforecast.fragment.WeatherForecastDetailFragment;

import java.util.ArrayList;
import java.util.List;

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

    private static final String TAG = WeatherForecastDetailPagerActivity.class.getSimpleName();

    private static final String EXTRA_FORECAST_ID = "forecast_id";
    private static final String EXTRA_CITY_ID = "city_id";

    private ViewPager mViewPager;
    private List<WeatherForecastDao.WeatherForecastItemCity> mWeatherForecastItemsByCity;

    private String mForecastIdExtra;

    public static Intent newIntent(Context context, String forecastId, String cityId) {
        Intent intent = new Intent(context, WeatherForecastDetailPagerActivity.class);
        intent.putExtra(EXTRA_FORECAST_ID, forecastId);
        intent.putExtra(EXTRA_CITY_ID, cityId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_detail_viewpager);

        Toolbar toolbar = findViewById(R.id.viewpager_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // Enable the Up Button

        mForecastIdExtra = getIntent().getStringExtra(EXTRA_FORECAST_ID);
        String cityId = getIntent().getStringExtra(EXTRA_CITY_ID);

        final WeatherForecastViewModel weatherForecastViewModel;
        weatherForecastViewModel = ViewModelProviders.of(this)
                .get(WeatherForecastViewModel.class);

        weatherForecastViewModel.getWeatherForecastItemsByCity(cityId).observe(
            this, new Observer<List<WeatherForecastDao.WeatherForecastItemCity>>() {
                @Override
                public void onChanged(@Nullable List<WeatherForecastDao.WeatherForecastItemCity>
                                              weatherForecastItems) {

                    if (!weatherForecastItems.isEmpty()) {

                        mWeatherForecastItemsByCity = weatherForecastItems;

                        actionBar.setTitle(mWeatherForecastItemsByCity.get(0).
                                weatherForecastItem.getCityName());

                        String subtitle;
                        if (mWeatherForecastItemsByCity.get(0).getCityState().isEmpty()) {
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
                                return mWeatherForecastItemsByCity.size();
                            }
                        });

                        // Set current viewPagerItem to the clicked recyclerView item
                        for (int i = 0; i < mWeatherForecastItemsByCity.size(); i++) {
                            if (mWeatherForecastItemsByCity.get(i).
                                    weatherForecastItem.getForecastId().equals(mForecastIdExtra)) {
                                mViewPager.setCurrentItem(i);
                                break;
                            }
                        }


                    }
                }
            });


    }
}

