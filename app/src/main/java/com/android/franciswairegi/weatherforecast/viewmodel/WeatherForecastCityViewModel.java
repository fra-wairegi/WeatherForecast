package com.android.franciswairegi.weatherforecast.viewmodel;

import android.app.Application;

import com.android.franciswairegi.weatherforecast.model.WeatherForecastCityItem;
import com.android.franciswairegi.weatherforecast.roomrepository.WeatherForecastCityRepository;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WeatherForecastCityViewModel extends AndroidViewModel {

    private WeatherForecastCityRepository mWeatherCityRepository;
    private LiveData<List<WeatherForecastCityItem>> mAllCities; // Caches all cities

    public WeatherForecastCityViewModel(Application application){
        super(application);
        mWeatherCityRepository = WeatherForecastCityRepository.newInstance(application);
        mAllCities = mWeatherCityRepository.getAllCities();
    }

    public LiveData<List<WeatherForecastCityItem>> getAllCities(){
        return mAllCities;
    }

}
