package com.android.franciswairegi.weatherforecast;

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

    @Query("DELETE FROM cities")
    void deleteAll();

    @Insert
    void insertList(List<WeatherForecastCityItem> weatherForecastCityItems);

    @Query("SELECT * FROM cities ORDER BY city_name ASC, country_name DESC")
    LiveData<List<WeatherForecastCityItem>> getAllCities();

}
