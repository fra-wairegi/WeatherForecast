package com.android.franciswairegi.weatherforecast;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

@Entity(tableName = "cities")
public class WeatherForecastCityItem implements Parcelable {

    public static final String EXTRA_CITY_LIST = "weather_forecast_city_item_array_list";

    public WeatherForecastCityItem(){
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "city_id")
    private Integer mCityID;

    @ColumnInfo(name = "city_name")
    private String mCityName;

    @ColumnInfo(name = "country_name")
    private String mCityCountryName;

    @ColumnInfo(name = "city_timezone")
    private String mCityTimezone;

    @ColumnInfo(name = "state_code")
    private String mCityState;

    public Integer getCityID() {
        return mCityID;
    }

    public void setCityID(Integer cityID) {
        mCityID = cityID;
    }

    public String getCityName() {
        // Handle the "," for cities without state
        //if (mCityState.isEmpty()){
            return mCityName;
       // }
        //return mCityName + ", " + mCityState;
    }

    public String getCityStateName() {
        // Handle the "," for cities without state
        if (mCityState.isEmpty()){
        return mCityName;
         }
        return mCityName + ", " + mCityState;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public String getCityCountryName() {
        return mCityCountryName;
    }

    public void setCityCountryName(String cityCountryName) {
        mCityCountryName = cityCountryName;
    }

    public String getCityTimezone() {
        return mCityTimezone;
    }

    public void setCityTimezone(String cityTimezone) {
        mCityTimezone = cityTimezone;
    }

    public String getCityState() {
        return mCityState;
    }


    public void setCityState(String cityState) {
        mCityState = cityState;
    }



    @Override
    public void writeToParcel(Parcel out, int i) {

        out.writeInt(mCityID);
        out.writeString(mCityName);
        out.writeString(mCityCountryName);
        out.writeString(mCityTimezone);
        out.writeString(mCityState);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<WeatherForecastCityItem> CREATOR = new Parcelable.Creator<WeatherForecastCityItem>() {
        public WeatherForecastCityItem createFromParcel(Parcel in) {
            return new WeatherForecastCityItem(in);
        }
        public WeatherForecastCityItem[] newArray(int size) {
            return new WeatherForecastCityItem[size];
        }
    };

    private WeatherForecastCityItem(Parcel in) {

        mCityID = in.readInt();
        mCityName = in.readString();
        mCityCountryName = in.readString();
        mCityTimezone = in.readString();
        mCityState = in.readString();


    }
}
