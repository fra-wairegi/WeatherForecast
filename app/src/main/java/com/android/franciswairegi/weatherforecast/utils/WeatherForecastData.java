package com.android.franciswairegi.weatherforecast.utils;

import com.android.franciswairegi.weatherforecast.dao.WeatherForecastDao;

import java.util.ArrayList;
import java.util.List;

public class WeatherForecastData {

    private String mCity;
    private String mCountry;
    private String mStateCode;
    private List<SectionOrRow> mSectionOrRowItems;
    private List<WeatherForecastDao.WeatherForecastItemCity> mWeatherForecastItems =
            new ArrayList<>();

    public List<SectionOrRow> getSectionOrRowItems() {
        return mSectionOrRowItems;
    }

    public void setSectionOrRowItems(ArrayList<SectionOrRow> sectionOrRowItems) {

        for (int i = 0; i < sectionOrRowItems.size(); i++){
            if(sectionOrRowItems.get(i).isRow()) {
                mWeatherForecastItems.add(sectionOrRowItems.get(i).getRow());
            }
        }
        mSectionOrRowItems = sectionOrRowItems;
    }


    public List<WeatherForecastDao.WeatherForecastItemCity> getWeatherForecastItems() {
        return mWeatherForecastItems;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public void setStateCode(String stateCode) {
        mStateCode = stateCode;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getSubtitle(){
        if(!mStateCode.isEmpty()) {
            return mCity + " " + mStateCode + ", " + mCountry;
        } else {
            return mCity + ", " + mCountry;
        }
    }

    public void setCountry(String country) {
        mCountry = country;
    }

}
