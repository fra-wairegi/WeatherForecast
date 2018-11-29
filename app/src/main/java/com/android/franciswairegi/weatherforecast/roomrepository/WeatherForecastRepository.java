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

    private static final String TAG = WeatherForecastRepository.class.getSimpleName();

    private WeatherForecastDao mWeatherForecastDao;
    //private WeatherForecastCityItem mWeatherForecastCityItem;
    private Context mContext;

    private WeatherForecastDao.WeatherForecastItemCity mWeatherForecastItemForecastId;

    WeatherForecastRepository(Application application){
        Log.i(TAG, "TESTING1 Inside WeatherForecastRepository constructor");
        mContext = application;
        WeatherForecastRoomDatabase db = WeatherForecastRoomDatabase.getDatabase(application);

        mWeatherForecastDao = db.weatherForecastDao();
    }

    public static WeatherForecastRepository newInstance(Application application){
        return new WeatherForecastRepository(application);
    }

    // Wrapper for city Items
    public LiveData<List<WeatherForecastDao.WeatherForecastItemCity>> getWeatherForecastItemsbyCity(String cityId){
        Log.i(TAG,"TESTING1 in getWeatherForecastItemsbyCity cityId " + cityId);
        //mWeatherForecastCityItem = mWeatherForecastDao.getCityByCityId(cityId);
        return mWeatherForecastDao.getWeatherForecastByCityId(cityId);

    }

    public LiveData<WeatherForecastDao.WeatherForecastItemCity> getWeatherForecastByForecastId(String forecastId){
        Log.i(TAG,"TESTING1 in getWeatherForecastByForecastId forecastId " + forecastId);
        //mWeatherForecastCityItem = mWeatherForecastDao.getCityByCityId(cityId);
        return mWeatherForecastDao.getWeatherForecastByForecastId(forecastId);

    }

    // Wrapper for city Item
/*    WeatherForecastCityItem getCityByCityId(){
        Log.i(TAG,"TESTING1 in getCityByCityId cityId ");
        return mWeatherForecastCityItem;

    }*/

    public LiveData<List<WeatherForecastItem>> getAllWeatherForecast(){
        return mWeatherForecastDao.getAllWeatherForecast();

    }

 /*   Integer getCount(String cityId){
        return mWeatherForecastDao.getCount(cityId);
    }*/

    public void insert (String cityId) {
        Log.i("WordRepository", "TESTING1 inside insert()");
        //Utility utility = new Utility();
       // List<WeatherForecastItem> weatherForecastItems = utility.getWeatherForecastItems(cityId);

        //if (mWeatherForecastDao.getCount(cityId) == 0 ) {

            //new insertAsyncTask(mWeatherForecastDao, cityId).execute(weatherForecastItems);
        new insertAsyncTask(mWeatherForecastDao, cityId).execute();
        //}
    }

/*    public WeatherForecastDao.WeatherForecastItemCity queryByForecastId (String forecastId) {
        Log.i(TAG, "TESTING1 inside queryByForecastId()");
        //Utility utility = new Utility();
        // List<WeatherForecastItem> weatherForecastItems = utility.getWeatherForecastItems(cityId);

        //if (mWeatherForecastDao.getCount(cityId) == 0 ) {

        //new insertAsyncTask(mWeatherForecastDao, cityId).execute(weatherForecastItems);
        new queryAsyncTask(mWeatherForecastDao, forecastId).execute();
        return mWeatherForecastItemForecastId;
        //}
    }*/

    private class insertAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeatherForecastDao mAsyncTaskDao;
        private String mCityId;

        insertAsyncTask(WeatherForecastDao dao, String cityId) {
            mAsyncTaskDao = dao;
            mCityId = cityId;
        }

        @Override
       // protected Void doInBackground(final List<WeatherForecastItem>... params) {
        protected Void doInBackground(Void... params) {
            Utility utility = new Utility();

            //LiveData<WeatherForecastCityItem> mWeatherForecastCityItem;
            //mWeatherForecastCityItem = mAsyncTaskDao.getCityByCityId(mCityId);
            Log.i(TAG, "TESTING1 inside WordRepository doInBackground()");
            //if(mAsyncTaskDao.getCount(mCityId) == 0) {
            utility.getWeatherForecastItems(mCityId, mAsyncTaskDao, mContext);
            Log.i(TAG, "TESTING1 inside WordRepository doInBackground() if");
            //mAsyncTaskDao.insertList(weatherForecastItems);

            return null;
        }
    }

/*    private class queryAsyncTask extends
            AsyncTask<Void, Void, WeatherForecastDao.WeatherForecastItemCity> {

        private WeatherForecastDao mAsyncTaskDao;
        private String mForecastId;

        queryAsyncTask(WeatherForecastDao dao, String forecastId) {
            mAsyncTaskDao = dao;
            mForecastId = forecastId;
        }

        @Override
        // protected Void doInBackground(final List<WeatherForecastItem>... params) {
        protected WeatherForecastDao.WeatherForecastItemCity doInBackground(Void... params) {

            Log.i(TAG, "TESTING1 inside queryAsyncTask doInBackground()");
            mAsyncTaskDao.getWeatherForecastByForecastId(mForecastId);
            Log.i(TAG, "TESTING1 inside queryAsyncTask doInBackground() if");
            //mAsyncTaskDao.insertList(weatherForecastItems);

            return mAsyncTaskDao.getWeatherForecastByForecastId(mForecastId);
        }

        @Override
        protected void onPostExecute(WeatherForecastDao.WeatherForecastItemCity weatherForecastItemForecastId) {
            Log.i(TAG, "TESTING1 inside onPostExecute doInBackground()");
            mWeatherForecastItemForecastId = weatherForecastItemForecastId;
        }
    }*/


}
