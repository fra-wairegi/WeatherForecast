package com.android.franciswairegi.weatherforecast;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import android.util.Log;

import java.util.List;

public class WeatherForecastViewModel extends AndroidViewModel {

    private static final String TAG = WeatherForecastViewModel.class.getSimpleName();

    private WeatherForecastRepository mWeatherForecastRepository;

    public WeatherForecastViewModel(Application application){
        super(application);
        Log.i(TAG, "TESTING1 Inside WeatherForecastViewModel constructor");
        mWeatherForecastRepository = new WeatherForecastRepository(application);
    }

    public void insert(String cityId){
        mWeatherForecastRepository.insert(cityId);
    }

  /*  Integer getCount(String cityId){
        return mWeatherForecastRepository.getCount(cityId);
    }*/

    // Wrapper to get Weather Forecast for city
    LiveData<List<WeatherForecastDao.WeatherForecastItemCity>> getWeatherForecastItemsByCity(String cityId){
        Log.i(TAG,"TESTING1 in getWeatherForecastItemsByCity cityId " + cityId);
    /*    Log.i(TAG,"TESTING1 in getWeatherForecastItemsByCity mWeatherForecastRepository." +
                "getWeatherForecastItemsbyCity(cityId).getValue().size() " +
                mWeatherForecastRepository.getWeatherForecastItemsbyCity(cityId).getValue().size());*/
        return mWeatherForecastRepository.getWeatherForecastItemsbyCity(cityId);
    }

    LiveData<WeatherForecastDao.WeatherForecastItemCity> getWeatherForecastByForecastId(String forecastId) {
        Log.i(TAG, "TESTING1 in getWeatherForecastByForecastId cityId " + forecastId);
    /*    Log.i(TAG,"TESTING1 in getWeatherForecastItemsByCity mWeatherForecastRepository." +
                "getWeatherForecastItemsbyCity(cityId).getValue().size() " +
                mWeatherForecastRepository.getWeatherForecastItemsbyCity(cityId).getValue().size());*/
        return mWeatherForecastRepository.getWeatherForecastByForecastId(forecastId);
    }

/*
    WeatherForecastCityItem getCityByCityId(){
        return mWeatherForecastRepository.getCityByCityId();
    }
*/

    LiveData<List<WeatherForecastItem>> getAllWeatherForecast(){
        Log.i(TAG,"TESTING1 in getWeatherForecastItemsbyCity cityId ");
/*        Log.i(TAG,"TESTING1 in getWeatherForecastItemsByCity mWeatherForecastRepository." +
                "getWeatherForecastItemsbyCity(cityId).getValue().size() " +
                mWeatherForecastRepository.getAllWeatherForecast().getValue().size());*/
        return mWeatherForecastRepository.getAllWeatherForecast();

    }

}
