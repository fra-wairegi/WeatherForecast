package com.android.franciswairegi.weatherforecast;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "weather_forecast")
public class WeatherForecastItem  {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "forecast_id")
    private String mForecastId;

    @ColumnInfo(name = "dt")
    private Long mDateForecasted;

    @ColumnInfo(name = "weather_main")
    private String mWeatherMain;

    @ColumnInfo(name = "weather_description")
    private String mWeatherDescription;

    @ColumnInfo(name = "weather_icon")
    private String mWeatherIcon;

    @ColumnInfo(name = "temperature")
    private Double mTemperature;

    @ColumnInfo(name = "temperature_high")
    private Double mTemperatureHigh;

    @ColumnInfo(name = "temperature_low")
    private Double mTemperatureLow;

    @ColumnInfo(name = "pressure")
    private Double mPressure;

    @ColumnInfo(name = "wind_speed")
    private Double mWindSpeed;

    @ColumnInfo(name = "humidity")
    private Integer mHumidity; // Obtained in %

    @ColumnInfo(name = "wind_direction")
    private Double mWindDirection;

    @ColumnInfo(name = "clouds")
    private Integer mClouds;  // Obtained in %

    @ColumnInfo(name = "city_name")
    private String mCityName;

    @ColumnInfo(name = "city_id")
    private String mCityId;

  /*  @ColumnInfo(name = "state_code")
    private String mStateCode;*/

    @ColumnInfo(name = "country_code")
    private String mCountryCode;

    @ColumnInfo(name = "latitude")
    private Double mLatitude;

    @ColumnInfo(name = "longitude")
    private Double mLongitude;

    // Not needed to be a database column
    // Its valued is obtained from a subquery with the cities table
    @Ignore
    private String mTimeZone;

    @Ignore
    private Double mTemperatureCelcius;


/*    @Embedded(prefix = "city_")
    WeatherForecastCityItem mWeatherForecastCityItem;*/

/*    @ColumnInfo(name = "city_state_code")
    private String mCityStateCode;


    public String getStateCode(){
        return mCityStateCode;
    }*/
/*
    public WeatherForecastCityItem getWeatherForecastCityItem() {
        return mWeatherForecastCityItem;
    }

    public void setWeatherForecastCityItem(WeatherForecastCityItem weatherForecastCityItem) {
        this.mWeatherForecastCityItem = weatherForecastCityItem;
    }*/


  /*  // Constructor to set the mId
    public WeatherForecastItem(){
        this(UUID.randomUUID());
        //mId = UUID.randomUUID();
    }


    public WeatherForecastItem(UUID id){
        mForecastId = id.toString();
    }*/


    public void setForecastId(String forecastId){
        mForecastId = forecastId;
    }

    public String getForecastId() {
        return mForecastId;
    }

    public Long getDateForecasted() {
        return mDateForecasted;
    }

    public void setDateForecasted(Long dateForecasted) {
        mDateForecasted = dateForecasted;
    }

    public String getWeatherMain() {
        return mWeatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        mWeatherMain = weatherMain;
    }

    public String getWeatherDescription() {
        return mWeatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        mWeatherDescription = weatherDescription;
    }

    public String getWeatherIcon() {
        return mWeatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        mWeatherIcon = weatherIcon;
    }

    public Double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(Double temperature) {

        mTemperature = temperature;
    }

    public Double getTemperatureHigh() {
        return mTemperatureHigh;
    }

    public void setTemperatureHigh(Double temperatureHigh) {
        mTemperatureHigh = temperatureHigh;
    }

    public Double getTemperatureLow() {
        return mTemperatureLow;
    }

    public void setTemperatureLow(Double temperatureLow) {
        mTemperatureLow = temperatureLow;
    }

    public Double getPressure() {
        return mPressure;
    }

    public void setPressure(Double pressure) {
        mPressure = pressure;
    }

    public Double getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        mWindSpeed = windSpeed;
    }

    public Integer getHumidity() {
        return mHumidity;
    }

    public void setHumidity(int humidity) {
        mHumidity = humidity;
    }

    public Double getWindDirection() {
        return mWindDirection;
    }

    public void setWindDirection(Double windDirection) {
        mWindDirection = windDirection;
    }

    public Integer getClouds() {
        return mClouds;
    }

    public void setClouds(int clouds) {
        mClouds = clouds;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public String getCityId() {
        return mCityId;
    }

    public void setCityId(String cityId) {
        mCityId = cityId;
    }

/*    public String getStateCode() {
        return mWeatherForecastCityItem.getCityState();
    }

    public void setStateCode(String stateCode) {
        mWeatherForecastCityItem.setCityState(stateCode);
    }*/

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }


    public Double getTemperatureCelcius() {
        return mTemperatureCelcius;
    }

    public void setTemperatureCelcius(Double temperatureCelcius) {
        mTemperatureCelcius = temperatureCelcius;
    }
}
