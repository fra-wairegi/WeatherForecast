package com.android.franciswairegi.weatherforecast;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Database(entities = {WeatherForecastCityItem.class, WeatherForecastItem.class}, version = 1)
public abstract class WeatherForecastRoomDatabase extends RoomDatabase {

    private static final String TAG = WeatherForecastRoomDatabase.class.getSimpleName();

    public abstract WeatherForecastCityDao weatherForecastCityDao();

    public abstract WeatherForecastDao weatherForecastDao();

    private static Context mContext;

    private static volatile WeatherForecastRoomDatabase INSTANCE;
    private static QueryCityPreferences mQueryCityPreferences;


    static WeatherForecastRoomDatabase getDatabase(final Context context) {


        mContext = context;
        if (INSTANCE == null) {
            synchronized (WeatherForecastRoomDatabase.class) {
                if (INSTANCE == null) {
                    Log.i(TAG, "TESTING1 Inside getDatabase()");
                    // Create Database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherForecastRoomDatabase.class, "weather_forecast.db")
                            .addCallback(sRoomDatabaseCallback) // Populates database whenever the
                                                                  // app is started
                            //.allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final WeatherForecastCityDao mPopulateDbAsyncCityDao;
        private final WeatherForecastDao mPopulateDbAsyncDao;

        private Utility utility = new Utility();

        PopulateDbAsync(WeatherForecastRoomDatabase db) {
            Log.i(TAG, "TESTING1 Inside PopulateDbAsnyc constructor");
            mPopulateDbAsyncCityDao = db.weatherForecastCityDao();
            mPopulateDbAsyncDao = db.weatherForecastDao();
        }


        @Override
        protected Void doInBackground(final Void... params) {

            populateCities();

            Log.i(TAG, "Just before  utility.getWeatherForecastItems(Utility.DEFAULT_CITY_ID " +
                    Utility.DEFAULT_CITY_ID);
            //mPopulateDbAsyncDao.deleteAll();

            Log.i(TAG, "mPopulateDbAsyncDao.getCount(Utility.DEFAULT_CITY_ID) " +
                    mPopulateDbAsyncDao.getCount(Utility.DEFAULT_CITY_ID));

/*            if (mPopulateDbAsyncDao.getCount(Utility.DEFAULT_CITY_ID) == 0) {
                populateWeatherForecast(utility.getWeatherForecastItems(Utility.DEFAULT_CITY_ID));
            }*/


            // Count of records whose data is a forecast i.e future date
            // This count is used to delete stale data from the database.
            // The openWeather API returns 40 records for every fresh call.
            // If the count is less than 37 then the data is stale. It means
            // the data was updated at least 6 hours ago.

            /*
             * Commented as code has been taken to the utility class
            if (mPopulateDbAsyncDao.getValidForecastCount(Utility.DEFAULT_CITY_ID) < 37) {
                mPopulateDbAsyncDao.deleteAll();*/

                // Checking the count and inserting
                utility.getWeatherForecastItems(
                        Utility.DEFAULT_CITY_ID,
                        mPopulateDbAsyncDao,
                        mContext);

                // Commented on Oct 20. insertion being done by the utility class.
             /*   populateWeatherForecast(utility.getWeatherForecastItems(
                        Utility.DEFAULT_CITY_ID,
                        mPopulateDbAsyncDao,
                        mContext));*/

/*            PopulateWeatherForecast populateWeatherForecast =
                    new PopulateWeatherForecast(mContext);

            Log.i(TAG, "QueryCityPreferences.getStoredCityId(mContext)" +
                    QueryCityPreferences.getStoredCityId(mContext));

            populateWeatherForecast.insertWeatherForecastDataToDb(
                    QueryCityPreferences.getStoredCityId(mContext));*/

            return null;
        }

        private void populateCities(){

            List<WeatherForecastCityItem> weatherForecastCityItems = new ArrayList<>();

            Log.i(TAG,"TESTING1 Count in database is " + mPopulateDbAsyncCityDao.getCount());

            //mPopulateDbAsyncCityDao.deleteAll();
            if(mPopulateDbAsyncCityDao.getCount() == 0) {

                try {

                    Resources resources = mContext.getApplicationContext().getResources();
                    //InputStream in = resources.openRawResource(R.raw.partial_city_list);
                    InputStream in = resources.openRawResource(R.raw.city_list_v1);
                    Utility utility = new Utility();


     /*               ByteArrayOutputStream out = new ByteArrayOutputStream();
                    int bytesRead = 0;
                    byte[] b = new byte[in.available()];
                    //byte[] b = new byte[1024];
                    while ((bytesRead = in.read(b)) > 0) {
                        out.write(b, 0, bytesRead);
                    }
                    out.close();*/

                    //String jsonStringFromResource = new String(out.toByteArray());
                    String jsonStringFromResource = new String(utility.getBytes(in));

                    Log.i(TAG, "Received JSON: " + jsonStringFromResource);

                    JSONArray jsonArray = new JSONArray(jsonStringFromResource);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject listJsonObject = jsonArray.getJSONObject(i);

                        WeatherForecastCityItem item = new WeatherForecastCityItem();

                        item.setCityID(listJsonObject.getInt("id"));
                        item.setCityName(listJsonObject.getString("name"));
                        item.setCityCountryName(listJsonObject.getString("country"));
                        item.setCityTimezone(listJsonObject.getString("timezone"));
                        item.setCityState(listJsonObject.getString("state"));

                        weatherForecastCityItems.add(item);
                        //mPopulateDbAsyncDao.insert(item);
                    }

                    //mPopulateDbAsyncCityDao.deleteAll();
                    mPopulateDbAsyncCityDao.insertList(weatherForecastCityItems);

                    //return weatherForecastCityItems;

            /*    } catch (IOException ioe) {
                    Log.e(TAG, "Failed to read file ", ioe);
                    return null;*/
                } catch (JSONException jsone) {
                    Log.e(TAG, "Error with JSON Array ", jsone);
                    }
            }
        }

/*        private void populateWeatherForecast(List<WeatherForecastItem> weatherForecastItems){

            Log.i(TAG, "Inside populateWeatherForecast ");

            mPopulateDbAsyncDao.insertList(weatherForecastItems);

        }*/

    }


}
