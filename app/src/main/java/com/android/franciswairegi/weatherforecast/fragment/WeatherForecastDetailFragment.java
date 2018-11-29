package com.android.franciswairegi.weatherforecast.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.franciswairegi.weatherforecast.R;
import com.android.franciswairegi.weatherforecast.viewmodel.WeatherForecastViewModel;
import com.android.franciswairegi.weatherforecast.dao.WeatherForecastDao;
import com.android.franciswairegi.weatherforecast.utils.Utility;
import com.squareup.picasso.Picasso;

public  class WeatherForecastDetailFragment extends Fragment {

    private static final String TAG = WeatherForecastDetailFragment.class.getSimpleName();
    private static final String ARG_WEATHER_FORECAST_ID = "weather_forecast_id";
    private String mForecastId;

    private WeatherForecastViewModel mWeatherForecastViewModel;
    private WeatherForecastDao.WeatherForecastItemCity mWeatherForecastItemByForecastId;

    private TextView mDate, mTime, mWeatherDescription, mWeatherMain, mPressure, mPressureLabel;
    private TextView mTemperature, mTemperatureHighLow, mTemperatureHighLowLabel;
    private TextView mHumidity, mHumidityLabel, mWindSpeed, mWindSpeedLabel;
    private TextView mWindDirection, mWindDirectionLabel, mClouds, mCloudsLabel;
    private ImageView mWeatherIcon;

    public static WeatherForecastDetailFragment newInstance(
            String forecastId){

        Bundle arg = new Bundle();
        arg.putString(ARG_WEATHER_FORECAST_ID, forecastId);
        //arg.putParcelable(ARG_WEATHER_FORECAST_ITEM,weatherForecastItem);

        WeatherForecastDetailFragment weatherForecastDetailFragment =
                new WeatherForecastDetailFragment();
        weatherForecastDetailFragment.setArguments(arg);
        return weatherForecastDetailFragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mForecastId = getArguments().getString(ARG_WEATHER_FORECAST_ID);
        Log.i(TAG, "mForecastId " + mForecastId);

        mWeatherForecastViewModel = ViewModelProviders.of(this)
                .get(WeatherForecastViewModel.class);

        Log.i(TAG, "TESTING2 after ViewModelProviders.of ");

        mWeatherForecastViewModel.getWeatherForecastByForecastId(mForecastId).observe(
            //mWeatherForecastViewModel.getAllWeatherForecast().observe(
            this,
            new Observer<WeatherForecastDao.WeatherForecastItemCity>() {
                @Override
                public void onChanged(@Nullable WeatherForecastDao.WeatherForecastItemCity
                                              weatherForecastItemByForecastId) {

                    Log.i(TAG, "TESTING2 mWeatherForecastViewModel observe ");

                    Log.i(TAG, "weatherForecastItemByForecastId.getCountryName()"
                            + weatherForecastItemByForecastId.getCountryName());

                    Log.i(TAG, "weatherForecastItemByForecastId.getCountryName()"
                            + weatherForecastItemByForecastId.weatherForecastItem.getTemperature());

                    mWeatherForecastItemByForecastId = weatherForecastItemByForecastId;

                    Long longDate = weatherForecastItemByForecastId.
                            weatherForecastItem.getDateForecasted();

                    String timezone = weatherForecastItemByForecastId.getCityTimezone();

                    Log.i(TAG, "longDate in Detail " + longDate);
                    Log.i(TAG, "mForecastId in Detail " + mForecastId);
                    Log.i(TAG, "Displayed data in Detail " +
                    Utility.getDateForecastStr(getActivity(),longDate,weatherForecastItemByForecastId
                    .getCityTimezone()));


                    String dateForecastStr = Utility.getDateForecastStr(
                                                getActivity(),
                                                longDate,
                                                timezone);

                    String timeForecastedStr = Utility.getTimeForecastStr(
                                                getActivity(),
                                                longDate,
                                                timezone);

                    Log.i(TAG, "weatherForecastItemByForecastId.getCountryName()"
                            + mWeatherForecastItemByForecastId.weatherForecastItem.getCityName());
                    mDate.setText(dateForecastStr);
                    mTime.setText(timeForecastedStr);
                    mTemperature.setText(
                            Integer.toString(weatherForecastItemByForecastId.weatherForecastItem.
                            getTemperature().intValue()));
                    String temperatureHigh = Integer.toString(
                                                        weatherForecastItemByForecastId.
                                                        weatherForecastItem.
                                                        getTemperatureHigh().intValue());
                    String temperatureLow = Integer.toString(
                                                        weatherForecastItemByForecastId.
                                                        weatherForecastItem.
                                                        getTemperatureLow().intValue());
                    String temparatureHighLowStr = getString(
                                                        R.string.temperature_high_low_fahrenheit,
                                                        temperatureHigh,temperatureLow);
                    mTemperatureHighLow.setText(temparatureHighLowStr);

                    String weatherIcon = weatherForecastItemByForecastId.weatherForecastItem.
                            getWeatherIcon();
                    Picasso.get()
                            .load(Utility.getWeatherIconUrl(
                                    getActivity(), weatherIcon))
                            .into(mWeatherIcon);
                    String weatherDescription = weatherForecastItemByForecastId.
                                            weatherForecastItem.getWeatherDescription();
                    mWeatherDescription.setText(weatherDescription);

                    String pressure = weatherForecastItemByForecastId.
                                            weatherForecastItem.getPressure().toString();
                    String pressureLabel = getString(R.string.atmospheric_pressure_label);
                    String pressureUnit = getString(R.string.atmospheric_pressure_units);
                    String pressureStr = getString(R.string.weather_forecast_value_units,
                                                    pressure,
                                                    pressureUnit);
                    mPressure.setText(pressureStr);
                    mPressureLabel.setText(pressureLabel);

                    String humidity = weatherForecastItemByForecastId.
                            weatherForecastItem.getHumidity().toString();
                    String humidityLabel = getString(R.string.humidity_label);
                    String humidityUnit = getString(R.string.humidity_units);
                    String humidityStr = getString(R.string.weather_forecast_value_units,
                            humidity,
                            humidityUnit);
                    mHumidity.setText(humidityStr);
                    mHumidityLabel.setText(humidityLabel);

                    String windSpeed = weatherForecastItemByForecastId.
                            weatherForecastItem.getWindSpeed().toString();
                    String windSpeedLabel = getString(R.string.wind_speed_label);
                    String windSpeedUnit = getString(R.string.wind_speed_units);
                    String windSpeedStr = getString(R.string.weather_forecast_value_units,
                            windSpeed,
                            windSpeedUnit);
                    mWindSpeed.setText(windSpeedStr);
                    mWindSpeedLabel.setText(windSpeedLabel);

                    String windDirection = weatherForecastItemByForecastId.
                            weatherForecastItem.getWindDirection().toString();
                    String windDirectionLabel = getString(R.string.wind_direction_label);
                    String windDirectionUnit = getString(R.string.wind_direction_units);
                    String windDirectionStr = getString(R.string.weather_forecast_value_units,
                            windDirection,
                            windDirectionUnit);
                    mWindDirection.setText(windDirectionStr);
                    mWindDirectionLabel.setText(windDirectionLabel);

                    String clouds = weatherForecastItemByForecastId.
                            weatherForecastItem.getClouds().toString();
                    String cloudsLabel = getString(R.string.clouds_label);
                    String cloudsUnit = getString(R.string.clouds_units);
                    String cloudsStr = getString(R.string.weather_forecast_value_units,
                            clouds,
                            cloudsUnit);
                    mClouds.setText(cloudsStr);
                    mCloudsLabel.setText(cloudsLabel);
                }
            });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.weather_forecast_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(getActivity());
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "TESTING2 onCreateView ");
        return inflater.inflate(R.layout.fragment_detail_forecast, container, false);
        //return inflater.inflate(R.layout.fragment_detail_forecastv2, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mDayOfTheWeek = view.findViewById(R.id.day_of_the_week);
        //mDayOfTheWeek.setText("Today");
        Log.i(TAG, "TESTING2 onViewCreated ");
        mDate = view.findViewById(R.id.date);
        mTime = view.findViewById(R.id.time);
        //mTime.setText(mWeatherForecastItem.getTimeForecasted());
        mTemperature = view.findViewById(R.id.temperature);
        mTemperatureHighLow = view.findViewById(R.id.temperature_high_low);

        mWeatherIcon = view.findViewById(R.id.detail_weather_icon);
        mWeatherDescription = view.findViewById(R.id.weather_description);

        mPressure = view.findViewById(R.id.pressure);
        mPressureLabel = view.findViewById(R.id.pressure_label);

        mHumidity = view.findViewById(R.id.humidity);
        mHumidityLabel = view.findViewById(R.id.humidity_label);

        mWindSpeed = view.findViewById(R.id.wind_speed);
        mWindSpeedLabel = view.findViewById(R.id.wind_speed_label);

        mWindDirection = view.findViewById(R.id.wind_direction);
        mWindDirectionLabel = view.findViewById(R.id.wind_direction_label);

        mClouds = view.findViewById(R.id.clouds);
        mCloudsLabel = view.findViewById(R.id.clouds_label);
    }
}
