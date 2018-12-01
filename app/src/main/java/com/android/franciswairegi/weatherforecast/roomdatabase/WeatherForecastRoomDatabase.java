package com.android.franciswairegi.weatherforecast.roomdatabase;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import android.util.Log;

import com.android.franciswairegi.weatherforecast.R;
import com.android.franciswairegi.weatherforecast.dao.WeatherForecastCityDao;
import com.android.franciswairegi.weatherforecast.dao.WeatherForecastDao;
import com.android.franciswairegi.weatherforecast.model.WeatherForecastCityItem;
import com.android.franciswairegi.weatherforecast.model.WeatherForecastItem;
import com.android.franciswairegi.weatherforecast.utils.QueryCityPreferences;
import com.android.franciswairegi.weatherforecast.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Database(entities = {WeatherForecastCityItem.class, WeatherForecastItem.class}, version = 1)
public abstract class WeatherForecastRoomDatabase extends RoomDatabase {

    private static final String TAG = WeatherForecastRoomDatabase.class.getSimpleName();

    public abstract WeatherForecastCityDao weatherForecastCityDao();

    public abstract WeatherForecastDao weatherForecastDao();

    private static Context mContext;

    private static volatile WeatherForecastRoomDatabase INSTANCE;
    private static QueryCityPreferences mQueryCityPreferences;


    public static WeatherForecastRoomDatabase getDatabase(final Context context) {


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
            String defaultCityId = mContext.getString(R.string.city_id_default_value);
            utility.getWeatherForecastItems(
                    defaultCityId,
                    mPopulateDbAsyncDao,
                    mContext);

            // Commented on Oct 20. insertion being done by the utility class.
             /*   populateWeatherForecast(utility.getWeatherForecastItems(
                        Utility.DEFAULT_CITY_ID,
                        mPopulateDbAsyncDao,
                        mContext));*/

            return null;
        }

        private void populateCities() {

            List<WeatherForecastCityItem> weatherForecastCityItems = new ArrayList<>();

            Log.i(TAG, "TESTING1 Count in database is " + mPopulateDbAsyncCityDao.getCount());

            //mPopulateDbAsyncCityDao.deleteAll();
            if (mPopulateDbAsyncCityDao.getCount() == 0) {

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
