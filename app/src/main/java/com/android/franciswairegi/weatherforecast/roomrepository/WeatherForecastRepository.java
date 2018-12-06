package com.android.franciswairegi.weatherforecast.roomrepository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.franciswairegi.weatherforecast.roomdatabase.WeatherForecastRoomDatabase;
import com.android.franciswairegi.weatherforecast.dao.WeatherForecastDao;
import com.android.franciswairegi.weatherforecast.model.WeatherForecastItem;
import com.android.franciswairegi.weatherforecast.utils.Utility;

import java.util.List;

public class WeatherForecastRepository {

    /**
     * TAG for debugging
     * private static final String TAG = WeatherForecastRepository.class.getSimpleName();
     */

    private WeatherForecastDao mWeatherForecastDao;
    private static Context mContext;

    WeatherForecastRepository(Application application){
        mContext = application;
        WeatherForecastRoomDatabase db = WeatherForecastRoomDatabase.getDatabase(application);

        mWeatherForecastDao = db.weatherForecastDao();
    }

    public static WeatherForecastRepository newInstance(Application application){
        return new WeatherForecastRepository(application);
    }

    // Wrapper for city Items
    public LiveData<List<WeatherForecastDao.WeatherForecastItemCity>>
             getWeatherForecastItemsbyCity(String cityId){
        return mWeatherForecastDao.getWeatherForecastByCityId(cityId);

    }

    public LiveData<WeatherForecastDao.WeatherForecastItemCity>
             getWeatherForecastByForecastId(String forecastId){
        return mWeatherForecastDao.getWeatherForecastByForecastId(forecastId);

    }

    public LiveData<List<WeatherForecastItem>> getAllWeatherForecast(){
        return mWeatherForecastDao.getAllWeatherForecast();

    }

    public void insert (String cityId) {
        new insertAsyncTask(mWeatherForecastDao, cityId).execute();
    }

    private static class insertAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeatherForecastDao mAsyncTaskDao;
        private String mCityId;

        insertAsyncTask(WeatherForecastDao dao, String cityId) {
            mAsyncTaskDao = dao;
            mCityId = cityId;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Utility utility = new Utility();
            utility.getWeatherForecastItems(mCityId, mAsyncTaskDao, mContext);

            return null;
        }
    }
}
