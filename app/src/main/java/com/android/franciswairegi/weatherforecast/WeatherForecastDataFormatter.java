package com.android.franciswairegi.weatherforecast;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherForecastDataFormatter {

    private static final String TAG = "WeathForecastFormatter";

    private Context mContext;


    public WeatherForecastDataFormatter(Context context) {
        mContext = context;
    }


    private String getStartDate(){
        String longDate = mContext.getString(R.string.start_date);

    /*    SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(
                        mContext.getString(R.string.date_format));*/

        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(
                        mContext.getString(R.string.date_format));

        return simpleDateFormat.format(new Date(Long.parseLong(longDate)*1000L));
    }

    //public WeatherForecastData formatRecyclerViewItems(List<WeatherForecastItem> weatherForecastItems) {
    public WeatherForecastData formatRecyclerViewItems(
            List<WeatherForecastDao.WeatherForecastItemCity> weatherForecastItems) {

        Log.i(TAG, "Inside formatRecyclerViewItems " + weatherForecastItems.size());

        String currentItemDate = getStartDate();
        ArrayList<SectionOrRow> items = new ArrayList<>();
        WeatherForecastData weatherForecastData = new WeatherForecastData();

       //weatherForecastData.setCity(weatherForecastItems.get(0).getCityName());
       weatherForecastData.setCity(weatherForecastItems.get(0).weatherForecastItem.getCityName());
       //weatherForecastData.setCountry(weatherForecastItems.get(0).getCountryCode());
       weatherForecastData.setCountry(weatherForecastItems.get(0).weatherForecastItem.getCountryCode());
       weatherForecastData.setStateCode(
               weatherForecastItems.get(0).getCityState());

       // Obtain the city's timezone. It's the same for all records hence use get(0)
        String timezone = weatherForecastItems.get(0).getCityTimezone();


        for (int i = 0; i < weatherForecastItems.size(); i++) {
            //WeatherForecastItem weatherForecastItem = weatherForecastItems.get(i);
            //WeatherForecastItem weatherForecastItem = weatherForecastItems.get(i).weatherForecastItem;
            WeatherForecastDao.WeatherForecastItemCity weatherForecastItem =
                    weatherForecastItems.get(i);


            //WeatherForecastItem itemSection = new WeatherForecastItem();
            WeatherForecastDao.WeatherForecastItemCity itemSection =
                    new WeatherForecastDao.WeatherForecastItemCity();
            //WeatherForecastItem itemRow = new WeatherForecastItem();
            WeatherForecastDao.WeatherForecastItemCity itemRow
                    = new WeatherForecastDao.WeatherForecastItemCity();
            SectionOrRow section = new SectionOrRow();
            SectionOrRow row = new SectionOrRow();

            Log.i(TAG, "weatherForecastItem.weatherForecastItem.getForecastId " +
                    weatherForecastItem.weatherForecastItem.getForecastId());
            itemRow.weatherForecastItem.
                    setForecastId(weatherForecastItem.weatherForecastItem.getForecastId());
            itemRow.weatherForecastItem.
                    setCityId(weatherForecastItem.weatherForecastItem.getCityId());
            itemRow.weatherForecastItem.
                    setCityName(weatherForecastItem.weatherForecastItem.getCityName());
            itemRow.weatherForecastItem.
                    setCountryCode(weatherForecastItem.weatherForecastItem.getCountryCode());
            itemRow.weatherForecastItem.
                    setDateForecasted(weatherForecastItem.weatherForecastItem.getDateForecasted());

            itemRow.weatherForecastItem.
                    setWeatherIcon(weatherForecastItem.weatherForecastItem.getWeatherIcon());
            itemRow.weatherForecastItem.
                    setWeatherMain(weatherForecastItem.weatherForecastItem.getWeatherMain());
            itemRow.weatherForecastItem.
                    setWeatherDescription(weatherForecastItem.weatherForecastItem.getWeatherDescription());

            itemRow.weatherForecastItem.
                    setTemperature(weatherForecastItem.weatherForecastItem.getTemperature());
            itemRow.setTemperatureCelcius(weatherForecastItems.get(i).getTemperatureCelcius());
            itemRow.weatherForecastItem.
                    setTemperatureLow(weatherForecastItem.weatherForecastItem.getTemperatureLow());
            itemRow.weatherForecastItem.
                    setTemperatureHigh(weatherForecastItem.weatherForecastItem.getTemperatureHigh());
            itemRow.weatherForecastItem.
                    setPressure(weatherForecastItem.weatherForecastItem.getPressure());
            itemRow.weatherForecastItem.
                    setHumidity(weatherForecastItem.weatherForecastItem.getHumidity());
            itemRow.weatherForecastItem.
                    setWindSpeed(weatherForecastItem.weatherForecastItem.getWindSpeed());
            itemRow.weatherForecastItem.
                    setWindDirection(weatherForecastItem.weatherForecastItem.getWindDirection());

            itemRow.setCityTimezone(timezone);

            itemSection.weatherForecastItem.
                    setDateForecasted(weatherForecastItem.weatherForecastItem.getDateForecasted());
            itemSection.setCityTimezone(timezone);

            // Ensure date is inserted only once
            String dateForecasted = Utility.getDateForecastStr(
                    mContext,weatherForecastItem.weatherForecastItem.getDateForecasted(),timezone);

            if (!currentItemDate.equals(dateForecasted)) {
                section.createSection(itemSection);
                items.add(section);
                currentItemDate = dateForecasted;
            }

            row.createRow(itemRow);
            items.add(row);
            //weatherForecastItems.add(itemRow);
        }
        weatherForecastData.setSectionOrRowItems(items);

        Log.i(TAG, "End of formatRecyclerViewItems weatherForecastData.getSectionOrRowItems().size() " +
                weatherForecastData.getSectionOrRowItems().size());
        Log.i(TAG, "End of formatRecyclerViewItems weatherForecastData.getWeatherForecastItems().size() " +
                weatherForecastData.getWeatherForecastItems().size());
     return  weatherForecastData;
    }
}
