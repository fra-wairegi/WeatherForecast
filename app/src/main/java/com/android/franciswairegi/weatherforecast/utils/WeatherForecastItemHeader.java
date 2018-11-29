package com.android.franciswairegi.weatherforecast.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherForecastItemHeader {

    private long mDateForecasted;

    public WeatherForecastItemHeader(long dateForecasted){
        mDateForecasted = dateForecasted;
    }

    public String getDateForecasted() {
        Date date = new Date(mDateForecasted*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy ha z");
        return  simpleDateFormat.format(date);
    }
}
