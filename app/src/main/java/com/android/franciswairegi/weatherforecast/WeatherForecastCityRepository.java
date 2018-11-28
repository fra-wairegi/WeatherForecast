package com.android.franciswairegi.weatherforecast;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class WeatherForecastCityRepository {

    private WeatherForecastCityDao mCityDao;
    private LiveData<List<WeatherForecastCityItem>> mAllCities;

    WeatherForecastCityRepository(Application application){
        WeatherForecastRoomDatabase db = WeatherForecastRoomDatabase.getDatabase(application);
        mCityDao = db.weatherForecastCityDao();
        mAllCities = mCityDao.getAllCities();
        }

    // Wrapper for getAllCities
    LiveData<List<WeatherForecastCityItem>> getAllCities(){
        return mAllCities;
    }



    // Wrapper for insert
    // Done on a background thread as it is potentially a long running operation
    public void insert(WeatherForecastCityItem weatherForecastCityItem){
        new insertAsyncTask(mCityDao).execute(weatherForecastCityItem);
    }

    private static class insertAsyncTask extends AsyncTask<WeatherForecastCityItem, Void, Void>{
        private WeatherForecastCityDao mAsyncTaskDao;

        insertAsyncTask(WeatherForecastCityDao weatherForecastCityDao){
            mAsyncTaskDao = weatherForecastCityDao;
        }

        @Override
        protected Void doInBackground(final WeatherForecastCityItem... params){
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
