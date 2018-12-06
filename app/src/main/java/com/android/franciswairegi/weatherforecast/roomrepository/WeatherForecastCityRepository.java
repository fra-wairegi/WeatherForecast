package com.android.franciswairegi.weatherforecast.roomrepository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.android.franciswairegi.weatherforecast.roomdatabase.WeatherForecastRoomDatabase;
import com.android.franciswairegi.weatherforecast.dao.WeatherForecastCityDao;
import com.android.franciswairegi.weatherforecast.model.WeatherForecastCityItem;

import java.util.List;

public class WeatherForecastCityRepository {

    private WeatherForecastCityDao mCityDao;
    private LiveData<List<WeatherForecastCityItem>> mAllCities;

    WeatherForecastCityRepository(Application application){
        WeatherForecastRoomDatabase db = WeatherForecastRoomDatabase.getDatabase(application);
        mCityDao = db.weatherForecastCityDao();
        mAllCities = mCityDao.getAllCities();
        }

    public static WeatherForecastCityRepository newInstance(Application application){
        return new WeatherForecastCityRepository(application);
    }

    // Wrapper for getAllCities
    public LiveData<List<WeatherForecastCityItem>> getAllCities(){
        return mAllCities;
    }

}
