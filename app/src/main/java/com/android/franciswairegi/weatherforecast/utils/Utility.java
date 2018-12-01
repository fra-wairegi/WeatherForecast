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

    public static final String FAHRENHEIT = "f";
    public static final String CELCIUS = "c";

    public static byte[] getBytes(InputStream inputStream) {

        Log.i(TAG, "Inside Utility getBytes method " + inputStream.toString());

        try {


            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int bytesRead = 0;
            byte[] b = new byte[inputStream.available()];
            while ((bytesRead = inputStream.read(b)) > 0) {
                Log.i(TAG, "Inside Utility getBytes method " + inputStream.toString());
                out.write(b, 0, bytesRead);
            }
            Log.i(TAG, "Inside Utility Before out.close " + out.toString());
            out.close();
            Log.i(TAG, "Inside Utility after out.close out.toByteArray().length" + out.toByteArray().length);
            return out.toByteArray();
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to read file ", ioe);
            return null;
        }

    }


    public static String getDateForecastStr(Context context, Long dateForecasted, String timezoneStr){

   /*     Date date = new Date(dateForecasted*1000L);
        return  new SimpleDateFormat(context.getString(R.string.date_format)).format(date);*/

        Log.i(TAG, "Inside getDateForecastStr.Timezone is " + timezoneStr);

        long currentTime = System.currentTimeMillis();

        // The day string for forecast uses the following logic:
        // For today: "Today, Nov 3, 2018"
        // For tomorrow:  "Tomorrow, Nov 4, 2018"
        // For all days after that: "Mon Nov 5, 2018"

        Date forecastDateL = new Date(dateForecasted*1000L);
        Date forecastDate = new Date(dateForecasted);
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

        Log.i(TAG, "currentDayOfYear " + currentDayOfYear);
        Log.i(TAG, "forecastDayOfYear " + forecastDayOfYear);

        // Today's date. Format: "Today, Nov 3, 2018"
        if(currentDayOfYear == forecastDayOfYear){
            Log.i(TAG, "DateFormat 1 " + timezoneStr);
            Log.i(TAG, "return DateFormat 1 String " + context.getString(
                    R.string.full_formatted_date,
                    context.getString(R.string.today),
                    dateFormat));
            return context.getString(
                    R.string.full_formatted_date,
                    context.getString(R.string.today),
                    dateFormat);

        // Tomorrows's date. Format: "Tomorrow, Nov 4, 2018"
        } else if ((currentDayOfYear + 1) == forecastDayOfYear) {
            Log.i(TAG, "DateFormat 2 " + timezoneStr);
            Log.i(TAG, "return DateFormat 2 String " + context.getString(
                    R.string.full_formatted_date,
                    context.getString(R.string.tomorrow),
                    dateFormat));
            return context.getString(
                    R.string.full_formatted_date,
                    context.getString(R.string.tomorrow),
                    dateFormat);

            // For all days after that: "Mon Nov 5, 2018"
        } else {

            Log.i(TAG, "DateFormat 3 " + timezoneStr);

           /* String otherDaysFormat = context.getString(
                    R.string.full_formatted_date,
                    context.getString(R.string.other),
                    context.getString(R.string.date_format));*/
            SimpleDateFormat simpleDateFormatOther =
                    new SimpleDateFormat(context.getString(R.string.date_format));
            simpleDateFormatOther.setTimeZone(timezone);
            return simpleDateFormatOther.format(forecastDateL);
        }

/*        Log.i(TAG, "calendar.get forecastDate " + calendar.get(Calendar.DAY_OF_YEAR));
        calendar.setTime(currentDate);
        Log.i(TAG, "calendar.get currentDate " + calendar.get(Calendar.DAY_OF_YEAR));



        simpleDateFormat.setTimeZone(timezone);



        return simpleDateFormat.format(forecastDate);*/

    }


    public static String getTimeForecastStr(Context context, Long dateForecasted, String timezoneStr) {

        /*Date date = new Date(dateForecasted*1000L);
        return  new SimpleDateFormat(context.getString(R.string.time_format)).format(date);*/

        Log.i(TAG, "Inside getTimeForecastStr. Timezone is " + timezoneStr);

        Date date = new Date(dateForecasted*1000L);
        TimeZone timezone = TimeZone.getTimeZone(timezoneStr);
        //TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(context.getString(R.string.time_format));
        simpleDateFormat.setTimeZone(timezone);


        return simpleDateFormat.format(date);

    }

    public static Integer farenheitToCelcius(Integer temperature){
        return ((temperature - 32) * 5 / 9);
    }

    public static String getWeatherIconUrl(Context context, String weatherIcon){
        Log.i(TAG,"weatherIcon " + weatherIcon);
        String iconUrl = context.getString(R.string.weather_icon_base_url)+weatherIcon+
                context.getString(R.string.weather_icon_base_url_extension);
        Log.i(TAG,"iconUrl " + iconUrl);
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

        Log.i(TAG, "weatherForecastDao.getValidForecastCount(cityId) " +
                weatherForecastDao.getValidForecastCount(cityId) + "for city: " + cityId);

        if(weatherForecastDao.getValidForecastCount(cityId)
                < resources.getInteger(R.integer.valid_forecast_count)) {

            // Delete city data that is outdated.
            weatherForecastDao.deleteCity(cityId);

            Log.i(TAG, "getWeatherForecastItems cityId " + cityId);
            PopulateWeatherForecast populateWeatherForecast = new PopulateWeatherForecast();
            //return populateWeatherForecast.downloadWeatherForecastItems(cityId);
            weatherForecastDao.insertList(
                    populateWeatherForecast.downloadWeatherForecastItems(cityId));
        }

    }





    private class PopulateWeatherForecast {

        private static final String TAG = "FetchWeatherData";

        //private Context mContext;

        private final Uri FORECAST_BASE_URL = Uri.parse("http://api.openweathermap.org/data/2.5/forecast?")
                .buildUpon()
                //.appendQueryParameter("id", "5358705") // Huntington Beach
                //.appendQueryParameter("id", "184745") // Nairobi
                // .appendQueryParameter("id", "5140405") // Syracuse
                .appendQueryParameter("mode", "json")
                .appendQueryParameter("units", "imperial")
                .appendQueryParameter("cnt", Integer.toString(50))
                .appendQueryParameter("APPID", BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .build();

   /*     public PopulateWeatherForecast (Context context) {
            mContext = context;
        }*/

        public byte[] getUrlBytes(String urlSpec) throws IOException {
            URL url = new URL(urlSpec);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            Log.i(TAG, "Inside getUrlBytes method " + urlSpec);

/*                try {
                    InputStream in = connection.getInputStream();
                    Utility utility = new Utility();
                    Log.i(TAG, "InputStream " + in.toString());
                    Log.i(TAG, "InputStream available " + in.available());

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.i(TAG, "Inside if (connection.getResponseCode() " + urlSpec);
                        throw new IOException((connection.getResponseMessage() +
                                ": with " + urlSpec));
                    }
                    Log.i(TAG, "After if (connection.getResponseCode() " + urlSpec);

                    return utility.getBytes(in);

                } finally {
                    connection.disconnect();
                }*/

            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = connection.getInputStream();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException((connection.getResponseMessage() +
                            ": with " + urlSpec));
                }
                Log.i(TAG, "OpenWeather connected " + HttpURLConnection.HTTP_OK);

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

        // Obtain Json file from Resources
/*            private byte[] getJsonFromResources()  {


                Resources resources = mContext.getResources();
                InputStream in = resources.openRawResource(R.raw.five_day_json);
                Utility helperByteArray = new Utility(in);

                return helperByteArray.getBytes();

            }*/

        public String getUrlString(String urlSpec) throws IOException {
            return new String(getUrlBytes(urlSpec));
            //return new String(getJsonFromResources()); // Obtain file from resource
        }

        //public ArrayList<SectionOrRow> fetchWeatherForecastData() {
/*        public void insertWeatherForecastDataToDb(String cityQuery) {
            Log.i(TAG,"TESTING1 cityQuery in insertWeatherForecastDataToDb() is  "
                    + cityQuery);

            Log.i(TAG,"TESTING1 Count in database in insertWeatherForecastDataToDb() "
                    + mPopulateDbAsyncDao.getCount(cityQuery));

            // mPopulateDbAsyncDao.deleteAll();
            if(mPopulateDbAsyncDao.getCount(cityQuery) == 0) {
                Log.i(TAG,"Count in database weather forecast");
                String url = buildUrl(cityQuery);
                downloadWeatherForecastItems(url);
            }
        }*/

        //private ArrayList<SectionOrRow> downloadGalleryItems(String url) {
        private List<WeatherForecastItem> downloadWeatherForecastItems(String cityId) {

            List<WeatherForecastItem> weatherForecastItems = new ArrayList<>();

            String url = buildUrl(cityId);

            //ArrayList<SectionOrRow> items = new ArrayList<>();
                /*WeatherForecastData weatherForecastData =
                        new WeatherForecastData();*/

            try {
                Log.i(TAG, "url: " + url);
                String jsonString = getUrlString(url);

                Log.i(TAG, "Received JSON: " + jsonString);
                JSONObject jsonBody = new JSONObject(jsonString);
                //parseItems(items, jsonBody);
                parseItems(weatherForecastItems, jsonBody);
                //parseAndInsertItems(jsonBody);
                //weatherForecastData.setSectionOrRowItems(items);


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

            Log.i(TAG, "Built URL: " + uriBuilder.build().toString());
            return uriBuilder.build().toString();
        }

/*        private String getStartDate(){
            String longDate = mContext.getString(R.string.start_date);

            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat(mContext.getString(R.string.date_format));

            return simpleDateFormat.format(new Date(Long.parseLong(longDate)*1000L));
        }*/

        private void parseItems(List<WeatherForecastItem> weatherForecastItems, JSONObject jsonBody)
                throws JSONException {
        /*private void parseAndInsertItems(JSONObject jsonBody)
                throws JSONException {*/

            //String currentItemDate = getStartDate();
            //ArrayList<SectionOrRow> items = new ArrayList<>();

            // Array to be used to populate items
            //ArrayList<WeatherForecastItem> weatherForecastItems = new ArrayList<>();
            //List<WeatherForecastItem> weatherForecastItems = new ArrayList<>();

            JSONObject cityJsonObject = jsonBody.getJSONObject("city");
            JSONArray listJsonArray = jsonBody.getJSONArray("list");

            //weatherForecastData.setCity(cityJsonObject.getString("name"));
            //weatherForecastData.setCountry(cityJsonObject.getString("country"));


            for (int i = 0; i < listJsonArray.length(); i++) {
                JSONObject listJsonObject = listJsonArray.getJSONObject(i);

                JSONObject mainJsonObject = listJsonObject.getJSONObject("main");
                JSONArray weatherJsonArray = listJsonObject.getJSONArray("weather");

                //WeatherForecastItem itemSection = new WeatherForecastItem();
                WeatherForecastItem item = new WeatherForecastItem();
            /*SectionOrRow section = new SectionOrRow();
            SectionOrRow row = new SectionOrRow();*/

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
                // added on Nov 3 '18
                item.setClouds(cloudsJsonObject.getInt("all"));

                //itemSection.setDateForecasted(listJsonObject.getLong("dt"),mContext);

                // Ensure date is inserted only once
/*            String dateForecasted = itemSection.getDateForecasted();

            if (!currentItemDate.equals(dateForecasted)) {
                section.createSection(itemSection);
                items.add(section);
                currentItemDate = dateForecasted;
            }

            row.createRow(itemRow);*/
                //items.add(row);
                weatherForecastItems.add(item);
            }

            //mPopulateDbAsyncDao.insertList(weatherForecastItems);


            //weatherForecastData.setSectionOrRowItems(items);

        }
    }
}
