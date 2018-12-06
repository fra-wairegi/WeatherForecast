package com.android.franciswairegi.weatherforecast.utils;

import android.content.Context;
import android.util.Log;

import com.android.franciswairegi.weatherforecast.R;
import com.android.franciswairegi.weatherforecast.dao.WeatherForecastDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherForecastDataFormatter {

    /**
     * TAG used for debugging
     * private static final String TAG = WeatherForecastDataFormatter.class.getSimpleName();
     */

    private Context mContext;

    public WeatherForecastDataFormatter(Context context) {
        mContext = context;
    }


    private String getStartDate() {
        String longDate = mContext.getString(R.string.start_date);

        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(
                        mContext.getString(R.string.date_format));

        return simpleDateFormat.format(new Date(Long.parseLong(longDate) * 1000L));
    }

    public WeatherForecastData formatRecyclerViewItems(
            List<WeatherForecastDao.WeatherForecastItemCity> weatherForecastItems) {

        String currentItemDate = getStartDate();
        ArrayList<SectionOrRow> items = new ArrayList<>();
        WeatherForecastData weatherForecastData = new WeatherForecastData();

        weatherForecastData.setCity(
                weatherForecastItems.get(0).weatherForecastItem.getCityName());
        weatherForecastData.setCountry(
                weatherForecastItems.get(0).weatherForecastItem.getCountryCode());
        weatherForecastData.setStateCode(
                weatherForecastItems.get(0).getCityState());

        // Obtain the city's timezone. It's the same for all records hence use get(0)
        String timezone = weatherForecastItems.get(0).getCityTimezone();


        for (int i = 0; i < weatherForecastItems.size(); i++) {
            WeatherForecastDao.WeatherForecastItemCity weatherForecastItem =
                    weatherForecastItems.get(i);

            WeatherForecastDao.WeatherForecastItemCity itemSection =
                    new WeatherForecastDao.WeatherForecastItemCity();
            WeatherForecastDao.WeatherForecastItemCity itemRow
                    = new WeatherForecastDao.WeatherForecastItemCity();
            SectionOrRow section = new SectionOrRow();
            SectionOrRow row = new SectionOrRow();

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
                    setWeatherDescription(weatherForecastItem.weatherForecastItem.
                            getWeatherDescription());

            itemRow.weatherForecastItem.
                    setTemperature(weatherForecastItem.weatherForecastItem.getTemperature());
            itemRow.setTemperatureCelcius(weatherForecastItems.get(i).getTemperatureCelcius());
            itemRow.weatherForecastItem.
                    setTemperatureLow(weatherForecastItem.weatherForecastItem.getTemperatureLow());
            itemRow.weatherForecastItem.
                    setTemperatureHigh(weatherForecastItem.weatherForecastItem.
                            getTemperatureHigh());
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
                    mContext,
                    weatherForecastItem.weatherForecastItem.getDateForecasted(),
                    timezone);

            if (!currentItemDate.equals(dateForecasted)) {
                section.createSection(itemSection);
                items.add(section);
                currentItemDate = dateForecasted;
            }

            row.createRow(itemRow);
            items.add(row);
        }
        weatherForecastData.setSectionOrRowItems(items);

        return weatherForecastData;
    }
}
