package com.android.franciswairegi.weatherforecast;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class SectionOrRow {

    private WeatherForecastDao.WeatherForecastItemCity mSection;
    private WeatherForecastDao.WeatherForecastItemCity mRow;
    private boolean isRow;

    public void createSection(WeatherForecastDao.WeatherForecastItemCity section) {
        mSection = section;
        isRow = false;
    }

    public void createRow(WeatherForecastDao.WeatherForecastItemCity row) {
        mRow = row;
        isRow = true;
    }

    public WeatherForecastDao.WeatherForecastItemCity getRow() {
        return mRow;
    }

    public WeatherForecastDao.WeatherForecastItemCity getSection() {
        return mSection;
    }

    public boolean isRow() {
        return isRow;
    }

    @Override
    public String toString() {
        return "SectionOrRow{" +
                "mSection='" + mSection.toString() + '\'' +
                ", mRow=" + mRow.toString() +
                ", isRow=" + isRow +
                '}';
    }

}
