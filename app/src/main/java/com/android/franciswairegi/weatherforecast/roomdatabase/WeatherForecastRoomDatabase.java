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

    public static WeatherForecastRoomDatabase getDatabase(final Context context) {

        mContext = context;
        String databaseName = mContext.getString(R.string.database_name);
        if (INSTANCE == null) {
            synchronized (WeatherForecastRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create Database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherForecastRoomDatabase.class, databaseName)
                            .addCallback(sRoomDatabaseCallback) // Populates database whenever the
                            // app is started
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
            // TODO defaultCityId to be set devices location
            String defaultCityId = mContext.getString(R.string.city_id_default_value);
            utility.getWeatherForecastItems(
                    defaultCityId,
                    mPopulateDbAsyncDao,
                    mContext);

            return null;
        }

        private void populateCities() {

            List<WeatherForecastCityItem> weatherForecastCityItems = new ArrayList<>();

            if (mPopulateDbAsyncCityDao.getCount() == 0) {

                try {

                    Resources resources = mContext.getApplicationContext().getResources();
                    InputStream in = resources.openRawResource(R.raw.city_list_v1);
                    Utility utility = new Utility();

                    String jsonStringFromResource = new String(utility.getBytes(in));

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
                    }

                    mPopulateDbAsyncCityDao.insertList(weatherForecastCityItems);

                } catch (JSONException jsone) {
                    Log.e(TAG, "Error with JSON Array ", jsone);
                }
            }
        }


    }


}
