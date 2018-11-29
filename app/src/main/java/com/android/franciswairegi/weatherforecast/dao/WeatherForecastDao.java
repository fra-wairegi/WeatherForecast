package com.android.franciswairegi.weatherforecast.dao;

import com.android.franciswairegi.weatherforecast.model.WeatherForecastItem;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Embedded;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeatherForecastDao {

    @Insert
    void insertList(List<WeatherForecastItem> weatherForecastItems);

    @Query("SELECT COUNT(*) FROM weather_forecast WHERE weather_forecast.city_id = :cityId")
        //@Query("SELECT COUNT(*) FROM weather_forecast WHERE city_name = 'Huntington Beach'")
    Integer getCount(String cityId);

    @Query("SELECT COUNT(*) " +
            "FROM weather_forecast " +
            "WHERE weather_forecast.city_id = :cityId " +
            "AND dt > (SELECT strftime('%s','now'))")
        // Count of records whose data is a forecast i.e future date
        // This count is used to delete stale data from the database.
        // If the count is less than 37 then the data is stale. It means
        // the data was updated at least 6 hours ago.
    Integer getValidForecastCount(String cityId);

    @Query("DELETE FROM weather_forecast" )
    void deleteAll();

    @Query("DELETE FROM weather_forecast " +
            "WHERE city_id = :cityId" )
    void deleteCity(String cityId);

/*    @Query("SELECT weather_forecast.*" +
            "FROM weather_forecast " +
            "WHERE weather_forecast.city_id == :cityId ")
    LiveData<List<WeatherForecastItem>> getWeatherForecastByCityId(String cityId);*/

    @Query("SELECT weather_forecast.*," +
            "cities.state_code, " +
            "cities.city_timezone, " +
            "cities.country_name, " +
            "((weather_forecast.temperature - 32) * 5 / 9) AS temperature_celcius, " +
            "((weather_forecast.temperature_high - 32) * 5 / 9) AS temperature_high_celcius, " +
            "((weather_forecast.temperature_low - 32) * 5 / 9) AS temperature_low_celcius " +
            "FROM weather_forecast " +
            "INNER JOIN cities " +
            "ON weather_forecast.city_id = cities.city_id " +
            "WHERE dt > (SELECT strftime('%s','now')) " +
            "AND weather_forecast.city_id == :cityId ")
        //LiveData<List<WeatherForecastItem>> getWeatherForecastByCityId(String cityId);
    LiveData<List<WeatherForecastItemCity>> getWeatherForecastByCityId(String cityId);

    @Query("SELECT weather_forecast.*," +
            "cities.state_code, " +
            "cities.city_timezone, " +
            "cities.country_name, " +
            "((weather_forecast.temperature - 32) * 5 / 9) AS temperature_celcius, " +
            "((weather_forecast.temperature_high - 32) * 5 / 9) AS temperature_high_celcius, " +
            "((weather_forecast.temperature_low - 32) * 5 / 9) AS temperature_low_celcius " +
            "FROM weather_forecast " +
            "INNER JOIN cities " +
            "ON weather_forecast.city_id = cities.city_id " +
            "WHERE dt > (SELECT strftime('%s','now')) " +
            "AND weather_forecast.forecast_id == :forecastId ")
        //LiveData<List<WeatherForecastItem>> getWeatherForecastByCityId(String cityId);
    LiveData<WeatherForecastItemCity> getWeatherForecastByForecastId(String forecastId);

    class WeatherForecastItemCity  {
        @Embedded
        public WeatherForecastItem weatherForecastItem = new WeatherForecastItem();
        /*@Embedded(prefix = "city_")
        public WeatherForecastCityItem weatherForecastCityItem;*/
        @ColumnInfo(name = "state_code")
        private String mCityState;

        @ColumnInfo(name = "country_name")
        private String mCountryName;

        @ColumnInfo(name = "city_timezone")
        private String mCityTimezone;

        @ColumnInfo(name = "temperature_celcius")
        private Double mTemperatureCelcius;

        @ColumnInfo(name = "temperature_high_celcius")
        private Double mTemperatureHighCelcius;

        @ColumnInfo(name = "temperature_low_celcius")
        private Double mTemperatureLowCelcius;

        public String getCityState() {
            return mCityState;
        }

        //setter used by Room
        public void setCityState(String cityState) {
            mCityState = cityState;
        }

        public String getCountryName() {
            return mCountryName;
        }

        //setter used by Room
        public void setCountryName(String countryName) {
            mCountryName = countryName;
        }

        public String getCityTimezone() {
            return mCityTimezone;
        }

        public void setCityTimezone(String cityTimezone) {
            mCityTimezone = cityTimezone;
        }

        public Double getTemperatureCelcius() {
            return mTemperatureCelcius;
        }

        public void setTemperatureCelcius(Double temperatureCelcius) {
            mTemperatureCelcius = temperatureCelcius;
        }

        public Double getTemperatureHighCelcius() {
            return mTemperatureHighCelcius;
        }

        public void setTemperatureHighCelcius(Double temperatureHighCelcius) {
            mTemperatureHighCelcius = temperatureHighCelcius;
        }

        public Double getTemperatureLowCelcius() {
            return mTemperatureLowCelcius;
        }

        public void setTemperatureLowCelcius(Double temperatureLowCelcius) {
            mTemperatureLowCelcius = temperatureLowCelcius;
        }
    }

/*    @Query("SELECT cities.*" +
            "FROM cities " +
            "WHERE cities.city_id == :cityId ")
    WeatherForecastCityItem getCityByCityId(String cityId);*/

    //@Query("SELECT * FROM weather_forecast")
    @Query("SELECT * FROM weather_forecast WHERE weather_forecast.city_id IS NOT NULL ")
    LiveData<List<WeatherForecastItem>> getAllWeatherForecast();

}
