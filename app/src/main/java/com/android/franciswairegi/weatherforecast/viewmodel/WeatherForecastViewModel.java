package com.android.franciswairegi.weatherforecast.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.franciswairegi.weatherforecast.dao.WeatherForecastDao;
import com.android.franciswairegi.weatherforecast.roomrepository.WeatherForecastRepository;

import java.util.List;

public class WeatherForecastViewModel extends AndroidViewModel {

    /**
     * TAG used for debugging
     * private static final String TAG = WeatherForecastViewModel.class.getSimpleName();
     */

    private WeatherForecastRepository mWeatherForecastRepository;

    public WeatherForecastViewModel(Application application) {
        super(application);
        mWeatherForecastRepository = WeatherForecastRepository.newInstance(application);
    }

    public void insert(String cityId) {
        mWeatherForecastRepository.insert(cityId);
    }

    // Wrapper to get Weather Forecast for city
    public LiveData<List<WeatherForecastDao.WeatherForecastItemCity>>
    getWeatherForecastItemsByCity(String cityId) {

        return mWeatherForecastRepository.getWeatherForecastItemsbyCity(cityId);
    }

    public LiveData<WeatherForecastDao.WeatherForecastItemCity>
    getWeatherForecastByForecastId(String forecastId) {

        return mWeatherForecastRepository.getWeatherForecastByForecastId(forecastId);
    }

}
