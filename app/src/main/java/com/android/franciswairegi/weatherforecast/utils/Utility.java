package com.android.franciswairegi.weatherforecast.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.android.franciswairegi.weatherforecast.BuildConfig;
import com.android.franciswairegi.weatherforecast.R;
import com.android.franciswairegi.weatherforecast.dao.WeatherForecastDao;
import com.android.franciswairegi.weatherforecast.model.WeatherForecastItem;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class Utility {

    private static final String TAG = Utility.class.getSimpleName();
    private String mBaseUri;

    public static final String FAHRENHEIT = "f";
    public static final String CELCIUS = "c";

    public static byte[] getBytes(InputStream inputStream) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int bytesRead;
            byte[] b = new byte[inputStream.available()];
            while ((bytesRead = inputStream.read(b)) > 0) {
                out.write(b, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to read file ", ioe);
            return null;
        }

    }


    public static String getDateForecastStr(Context context,
                                            Long dateForecasted,
                                            String timezoneStr){

        long currentTime = System.currentTimeMillis();

        // The day string for forecast uses the following logic:
        // For today: "Today, Nov 3, 2018"
        // For tomorrow:  "Tomorrow, Nov 4, 2018"
        // For all days after that: "Mon Nov 5, 2018"

        Date forecastDateL = new Date(dateForecasted*1000L);
        Date currentDate = new Date(currentTime);

        TimeZone timezone = TimeZone.getTimeZone(timezoneStr);
        Calendar currentCalendar = Calendar.getInstance(timezone);
        Calendar forecastCalendar = Calendar.getInstance(timezone);


        currentCalendar.setTime(currentDate);
        forecastCalendar.setTime(forecastDateL);
        int currentDayOfYear = currentCalendar.get(Calendar.DAY_OF_YEAR);
        int forecastDayOfYear = forecastCalendar.get(Calendar.DAY_OF_YEAR);

        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(context.getString(R.string.date_format));
        simpleDateFormat.setTimeZone(timezone);
        String dateFormat = simpleDateFormat.format(forecastDateL);

        // Today's date. Format: "Today, Nov 3, 2018"
        if(currentDayOfYear == forecastDayOfYear){
            return context.getString(
                    R.string.full_formatted_date,
                    context.getString(R.string.today),
                    dateFormat);

        // Tomorrows's date. Format: "Tomorrow, Nov 4, 2018"
        } else if ((currentDayOfYear + 1) == forecastDayOfYear) {
            return context.getString(
                    R.string.full_formatted_date,
                    context.getString(R.string.tomorrow),
                    dateFormat);

        // For all days after that: "Mon Nov 5, 2018"
        } else {

            SimpleDateFormat simpleDateFormatOther =
                    new SimpleDateFormat(context.getString(R.string.date_format));
            simpleDateFormatOther.setTimeZone(timezone);
            return simpleDateFormatOther.format(forecastDateL);
        }

    }


    public static String getTimeForecastStr(Context context,
                                            Long dateForecasted,
                                            String timezoneStr) {

        Date date = new Date(dateForecasted*1000L);
        TimeZone timezone = TimeZone.getTimeZone(timezoneStr);
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(context.getString(R.string.time_format));
        simpleDateFormat.setTimeZone(timezone);

        return simpleDateFormat.format(date);
    }

    public static String getWeatherIconUrl(Context context, String weatherIcon){
        String iconUrl = context.getString(R.string.weather_icon_base_url)+weatherIcon+
                context.getString(R.string.weather_icon_base_url_extension);
        return iconUrl;
    }

    public void getWeatherForecastItems(String cityId,
            WeatherForecastDao weatherForecastDao, Context context){

        Resources resources = context.getResources();

        // Count of records whose data is a forecast i.e future date
        // This count is used to delete stale data from the database.
        // The openWeather API returns 40 records for every fresh call.
        // If the count is less than 37 then the data is stale. It means
        // the data was updated at least 6 hours ago.

        if(weatherForecastDao.getValidForecastCount(cityId)
                < resources.getInteger(R.integer.valid_forecast_count)) {

            // Delete city weather forecast data that is outdated.
            weatherForecastDao.deleteCity(cityId);
            mBaseUri = resources.getString(R.string.base_uri);
            PopulateWeatherForecast populateWeatherForecast = new PopulateWeatherForecast();
            weatherForecastDao.insertList(
                    populateWeatherForecast.downloadWeatherForecastItems(cityId));
        }

    }

    private class PopulateWeatherForecast {

        private static final String TAG = "PopulateWeatherForecast";

        private final Uri FORECAST_BASE_URL = Uri.parse(mBaseUri)
                .buildUpon()
                .appendQueryParameter("mode", "json")
                .appendQueryParameter("units", "imperial")
                .appendQueryParameter("cnt", Integer.toString(50))
                .appendQueryParameter("APPID", BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .build();

        public byte[] getUrlBytes(String urlSpec) throws IOException {
            URL url = new URL(urlSpec);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = connection.getInputStream();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException((connection.getResponseMessage() +
                            ": with " + urlSpec));
                }

                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                return out.toByteArray();
            } finally {
                connection.disconnect();
            }
        }

        public String getUrlString(String urlSpec) throws IOException {
            return new String(getUrlBytes(urlSpec));
        }

        private List<WeatherForecastItem> downloadWeatherForecastItems(String cityId) {

            List<WeatherForecastItem> weatherForecastItems = new ArrayList<>();

            String url = buildUrl(cityId);

            try {
                String jsonString = getUrlString(url);

                JSONObject jsonBody = new JSONObject(jsonString);
                parseItems(weatherForecastItems, jsonBody);


            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch items", ioe);
            } catch (JSONException je) {
                Log.e(TAG, "Failed to parse", je);
            }

            return weatherForecastItems;
        }

        private String buildUrl(String cityId) {

            Uri.Builder uriBuilder = FORECAST_BASE_URL.buildUpon()
                    .appendQueryParameter("id",cityId);
            return uriBuilder.build().toString();
        }


        private void parseItems(List<WeatherForecastItem> weatherForecastItems, JSONObject jsonBody)
                throws JSONException {

            JSONObject cityJsonObject = jsonBody.getJSONObject("city");
            JSONArray listJsonArray = jsonBody.getJSONArray("list");

            for (int i = 0; i < listJsonArray.length(); i++) {
                JSONObject listJsonObject = listJsonArray.getJSONObject(i);

                JSONObject mainJsonObject = listJsonObject.getJSONObject("main");
                JSONArray weatherJsonArray = listJsonObject.getJSONArray("weather");

                WeatherForecastItem item = new WeatherForecastItem();

                for (int j = 0; j < weatherJsonArray.length(); j++) {
                    JSONObject weatherJsonObject = weatherJsonArray.getJSONObject(j);

                    item.setWeatherMain(weatherJsonObject.getString("main"));
                    item.setWeatherDescription(weatherJsonObject.getString("description"));
                    item.setWeatherIcon(weatherJsonObject.getString("icon"));
                }

                JSONObject windJsonObject = listJsonObject.getJSONObject("wind");

                JSONObject cloudsJsonObject = listJsonObject.getJSONObject("clouds");

                item.setCityName(cityJsonObject.getString("name"));
                item.setCityId(cityJsonObject.getString("id"));
                item.setCountryCode(cityJsonObject.getString("country"));

                item.setCityName(cityJsonObject.getString("name"));
                item.setCountryCode(cityJsonObject.getString("country"));
                item.setDateForecasted(listJsonObject.getLong("dt"));
                item.setForecastId(UUID.randomUUID().toString());
                item.setTemperature(mainJsonObject.getDouble("temp"));
                item.setTemperatureLow(mainJsonObject.getDouble("temp_min"));
                item.setTemperatureHigh(mainJsonObject.getDouble("temp_max"));
                item.setPressure(mainJsonObject.getDouble("pressure"));
                item.setHumidity(mainJsonObject.getInt("humidity"));
                item.setWindSpeed(windJsonObject.getDouble("speed"));
                item.setWindDirection(windJsonObject.getDouble("deg"));
                item.setClouds(cloudsJsonObject.getInt("all"));

                weatherForecastItems.add(item);
            }
        }
    }
}
