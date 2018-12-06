package com.android.franciswairegi.weatherforecast.dao;

import com.android.franciswairegi.weatherforecast.model.WeatherForecastCityItem;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeatherForecastCityDao {

    @Insert
    void insert(WeatherForecastCityItem weatherForecastCityItem);

    @Query("SELECT COUNT(*) FROM cities")
    Integer getCount();

    @Insert
    void insertList(List<WeatherForecastCityItem> weatherForecastCityItems);

    @Query("SELECT * FROM cities ORDER BY city_name ASC, country_name DESC")
    LiveData<List<WeatherForecastCityItem>> getAllCities();

}
