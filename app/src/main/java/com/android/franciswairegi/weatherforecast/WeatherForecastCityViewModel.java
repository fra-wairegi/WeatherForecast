package com.android.franciswairegi.weatherforecast;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WeatherForecastCityViewModel extends AndroidViewModel {

    private WeatherForecastCityRepository mWeatherCityRepository;
    private LiveData<List<WeatherForecastCityItem>> mAllCities; // Caches all cities
    //private List<WeatherForecastCityItem> mCities; // Caches all cities

    public WeatherForecastCityViewModel(Application application){
        super(application);
        mWeatherCityRepository = new WeatherForecastCityRepository(application);
        mAllCities = mWeatherCityRepository.getAllCities();
        //mCities = mWeatherCityRepository.getCities();
    }

    LiveData<List<WeatherForecastCityItem>> getAllCities(){
        return mAllCities;
    }

/*    List<WeatherForecastCityItem> getCities(){
        return mCities;
    }*/

}
